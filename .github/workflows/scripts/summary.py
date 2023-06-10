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


def read_prop(file_name: str, key: str) -> str:
	with open(file_name) as prop:
		return next(filter(
			lambda l: l.split('=', 1)[0].strip() == key,
			prop.readlines()
		)).split('=', 1)[1].lstrip()


def get_sha256_hash(file_path: str) -> str:
	sha256_hash = hashlib.sha256()

	with open(file_path, 'rb') as f:
		for buf in iter(functools.partial(f.read, 4096), b''):
			sha256_hash.update(buf)

	return sha256_hash.hexdigest()


def main():
	target_subproject = os.environ.get('TARGET_SUBPROJECT', '')
	with open('.github/workflows/matrix_includes.json') as f:
		matrix: list[dict] = json.load(f)

	with open(os.environ['GITHUB_STEP_SUMMARY'], 'w') as f:
		f.write('## Build Artifacts Summary\n\n')
		f.write('| Subproject | for Minecraft | Files | SHA-256 |\n')
		f.write('| --- | --- | --- | --- |\n')

		for m in matrix:
			subproject = m['subproject_dir']
			if target_subproject != '' and subproject != target_subproject:
				continue
			game_versions = read_prop('versions/{}/gradle.properties'.format(subproject), 'game_versions')
			game_versions = game_versions.strip().replace('\\n', ', ')
			file_paths = glob.glob('build-artifacts/{}/build/libs/*.jar'.format(subproject))
			file_paths = list(filter(lambda fp: not fp.endswith('-sources.jar') and not fp.endswith('-dev.jar'), file_paths))
			if len(file_paths) == 0:
				file_name = '*not found*'
				sha256 = '*N/A*'
			else:
				file_name = '`{}`'.format(os.path.basename(file_paths[0]))
				sha256 = '`{}`'.format(get_sha256_hash(file_paths[0]))
			f.write('| {} | {} | {} | {} |\n'.format(subproject, game_versions, file_name, sha256))


if __name__ == '__main__':
	main()
