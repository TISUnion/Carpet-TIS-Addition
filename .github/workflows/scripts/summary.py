"""
A script to scan through all valid mod jars in build-artifacts.zip/$version/build/libs,
and generate an artifact summary table for that to GitHub action step summary
"""
__author__ = 'Fallen_Breath'

import functools
import glob
import hashlib
import json
import os

import jproperties


def read_prop(file_name: str, key: str) -> str:
	configs = jproperties.Properties()
	with open(file_name, 'rb') as f:
		configs.load(f)
	return configs[key].data


def get_sha256_hash(file_path: str) -> str:
	sha256_hash = hashlib.sha256()

	with open(file_path, 'rb') as f:
		for buf in iter(functools.partial(f.read, 4096), b''):
			sha256_hash.update(buf)

	return sha256_hash.hexdigest()


def main():
	warnings: list[str] = []

	target_subproject_env: str = os.environ.get('TARGET_SUBPROJECT', '')
	target_subprojects: list[str] = [x for x in target_subproject_env.split(',') if x]
	print('target_subprojects: {}'.format(target_subprojects))

	workflow_artifacts_json_str = os.environ.get('WORKFLOW_ARTIFACTS', '{"artifacts":[]}')
	artifacts: dict[str, dict] = {}
	try:
		artifacts = {artifact['name']: artifact for artifact in json.loads(workflow_artifacts_json_str)['artifacts']}
		print({artifact['name']: artifact['id'] for artifact in artifacts.values()})
	except Exception as e:
		warnings.append(f'Failed to parse workflow artifacts JSON: {e}, {workflow_artifacts_json_str!r}')

	with open('settings.json') as f:
		settings: dict = json.load(f)

	with open(os.environ['GITHUB_STEP_SUMMARY'], 'w') as f:
		f.write('## Build Artifacts Summary\n\n')
		f.write('| Subproject | For Minecraft | File | Size | SHA-256 |\n')
		f.write('| --- | --- | --- | --- | --- |\n')

		for subproject in settings['versions']:
			if len(target_subprojects) > 0 and subproject not in target_subprojects:
				print('skipping {}'.format(subproject))
				continue
			game_versions = read_prop(f'versions/{subproject}/gradle.properties', 'game_versions')
			game_versions = game_versions.strip().replace('\r', '').replace('\n', ', ')
			file_paths = glob.glob(f'build-artifacts/{subproject}/build/libs/*.jar')
			file_paths = [fp for fp in sorted(file_paths) if all(not fp.endswith(f'-{classifier}.jar') for classifier in ['sources', 'dev', 'shadow'])]
			if len(file_paths) == 0:
				file_name = '*not found*'
				sha256 = '*N/A*'
			else:
				file_name = '`{}`'.format(os.path.basename(file_paths[0]))
				file_size = '{} B'.format(os.path.getsize(file_paths[0]))
				sha256 = '`{}`'.format(get_sha256_hash(file_paths[0]))
				if len(file_paths) > 1:
					warnings.append('Found too many build files in subproject {}: {}'.format(subproject, ', '.join(file_paths)))

			f.write(f'| {subproject} | {game_versions} | {file_name} | {file_size} | {sha256} |\n')
		f.write('\n')

		f.write('## Artifact Files\n\n')
		all_digests_are_sha256 = all(artifact['digest'].startswith('sha256:') for artifact in artifacts.values())
		f.write('| Artifact | For | Size | {} | \n'.format('SHA-256' if all_digests_are_sha256 else 'Digest'))
		f.write('| --- | --- | --- | --- |\n')
		for artifact_name, artifact_usage in [
			('mod-jars', 'Players who want to grab and install the mod jar into their Minecraft clients'),
			('build-artifacts', 'Mod developers who want to inspect the complete build artifacts'),
		]:
			artifact_display_name = f'`{artifact_name}`'
			artifact_size = 'unknown'
			artifact_digest = 'unknown'
			if artifact_name in artifacts:
				# https://docs.github.com/en/rest/actions/artifacts?apiVersion=2022-11-28#list-workflow-run-artifacts
				artifact: dict = artifacts[artifact_name]
				try:
					download_url = f'{os.environ["GITHUB_SERVER_URL"]}/{os.environ["GITHUB_REPOSITORY"]}/actions/runs/{os.environ["GITHUB_RUN_ID"]}/artifacts/{artifact["id"]}'
					artifact_display_name = f'[`{artifact_name}`]({download_url})'
					artifact_size = f'{artifact["size_in_bytes"]} B'
					artifact_digest = '`{}`'.format(artifact["digest"].split(':', 1)[-1] if all_digests_are_sha256 else artifact["digest"])
				except Exception as e:
					warnings.append(f'Failed to collect artifact info for {artifact_name}: {e} -- {artifact}')

			f.write(f'| {artifact_display_name} | {artifact_usage} | {artifact_size} | {artifact_digest} |\n')
		f.write('\n')

		if len(warnings) > 0:
			f.write('## Warnings\n\n')
			for warning in warnings:
				f.write('- {}\n'.format(warning))
			f.write('\n')


if __name__ == '__main__':
	main()
