"""
A script to scan through all valid mod jars in build-artifacts.zip/$version/build/libs,
and generate an artifact summary table for that to GitHub action step summary
"""
__author__ = 'Fallen_Breath'

import glob
import json
import os


def read_prop(file_name: str, key: str) -> str:
	with open(file_name) as prop:
		return next(filter(
			lambda l: l.split('=', 1)[0].strip() == key,
			prop.readlines()
		)).split('=', 1)[1].lstrip()


target_subproject = os.environ.get('TARGET_SUBPROJECT', '')
with open('.github/workflows/matrix_includes.json') as f:
	matrix: list[dict] = json.load(f)

with open(os.environ['GITHUB_STEP_SUMMARY'], 'w') as f:
	f.write('## Build Artifacts Summary\n\n')
	f.write('| Subproject | for Minecraft | Files |\n')
	f.write('| --- | --- | --- |\n')

	for m in matrix:
		subproject = m['subproject_dir']
		if target_subproject != '' and subproject != target_subproject:
			continue
		game_versions = read_prop('versions/{}/gradle.properties'.format(subproject), 'game_versions')
		game_versions = game_versions.strip().replace('\\n', ', ')
		file_names = glob.glob('build-artifacts/{}/build/libs/*.jar'.format(subproject))
		file_names = ', '.join(map(
			lambda fn: '`{}`'.format(os.path.basename(fn)),
			filter(lambda fn: not fn.endswith('-sources.jar') and not fn.endswith('-dev.jar'), file_names)
		))
		f.write('| {} | {} | {} |\n'.format(subproject, game_versions, file_names))
