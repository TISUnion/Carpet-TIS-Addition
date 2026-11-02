"""
Generate the production server audit matrix and prepare its Fabric servers
"""
__author__ = 'Fallen_Breath'

import argparse
import json
import os
from pathlib import Path
import re
import shutil
from urllib.parse import quote
from urllib.request import Request, urlopen

import jproperties


def read_properties(path: Path) -> dict[str, str]:
	properties = jproperties.Properties()
	with path.open('rb') as f:
		properties.load(f)
	return {key: value.data for key, value in properties.items()}


def read_project_properties(subproject: str) -> dict[str, str]:
	properties = read_properties(Path('gradle.properties'))
	properties.update(read_properties(Path('versions') / subproject / 'gradle.properties'))
	return properties


def version_tuple(version: str) -> tuple[int, int, int]:
	parts = [int(part) for part in version.split('-', 1)[0].split('.')]
	return tuple((parts + [0, 0])[:3])


def write_outputs(**outputs):
	with open(os.environ['GITHUB_OUTPUT'], 'a', encoding='utf-8') as f:
		for key, value in outputs.items():
			line = f'{key}={value}'
			print('output:', line)
			f.write(line + '\n')


def generate_matrix():
	entries = []
	seen_subprojects = set()
	for jar in sorted(Path('versions').glob('*/build/libs/*.jar')):
		if jar.name.endswith(('-dev.jar', '-sources.jar', '-shadow.jar')):
			continue
		subproject = jar.relative_to('versions').parts[0]
		minecraft_version = read_project_properties(subproject)['minecraft_version']
		if version_tuple(minecraft_version) >= (26, 1, 0):
			continue
		if subproject in seen_subprojects:
			raise ValueError(f'Multiple mod jars found for subproject {subproject}')
		seen_subprojects.add(subproject)
		entries.append({
			'subproject': subproject,
			'minecraft_version': minecraft_version,
			'jar': jar.relative_to('versions').as_posix(),
		})
	write_outputs(
		matrix=json.dumps({'include': entries}),
		has_targets=str(bool(entries)).lower(),
	)


def download_json(url: str):
	request = Request(url, headers={'User-Agent': 'Carpet-TIS-Addition-CI'})
	with urlopen(request, timeout=60) as response:
		return json.load(response)


def download(url: str, path: Path):
	print(f'Downloading {url!r} to {path!r}')
	request = Request(url, headers={'User-Agent': 'Carpet-TIS-Addition-CI'})
	with urlopen(request, timeout=60) as response, path.open('wb') as f:
		shutil.copyfileobj(response, f)


def download_maven_jar(repository: str, coordinate: str, path: Path):
	group, artifact, version = coordinate.split(':')
	artifact_path = f'{group.replace(".", "/")}/{artifact}/{version}/{artifact}-{version}.jar'
	download(f'{repository}/{quote(artifact_path)}', path)


def prepare_server(subproject: str, jar: Path):
	properties = read_project_properties(subproject)
	minecraft_version = properties['minecraft_version']
	loader_version = properties['loader_version']

	server_dir = Path('server-mixin-audit')
	mods_dir = server_dir / 'mods'
	mods_dir.mkdir(parents=True, exist_ok=True)
	shutil.copy2(jar, mods_dir / jar.name)
	(server_dir / 'eula.txt').write_text('eula=true\n', encoding='utf8')

	manifest = download_json('https://piston-meta.mojang.com/mc/game/version_manifest_v2.json')
	try:
		version_url = next(version['url'] for version in manifest['versions'] if version['id'] == minecraft_version)
	except Exception:
		print(manifest)
		raise
	print(f'{version_url=}')
	version_info = download_json(version_url)
	java_version = version_info.get('javaVersion', {}).get('majorVersion', 25)
	print(f'{java_version=}')

	installers = download_json('https://meta.fabricmc.net/v2/versions/installer')
	try:
		installer_version = next(installer['version'] for installer in installers if installer['stable'])
	except Exception:
		print(f'{installers=}')
		raise
	download(
		f'https://meta.fabricmc.net/v2/versions/loader/{minecraft_version}/{loader_version}/{installer_version}/server/jar',
		server_dir / 'fabric-server-launch.jar',
	)

	carpet_version = properties['carpet_core_version']
	if re.fullmatch(r'com\.github\.[a-zA-Z0-9_-]+:fabric-carpet:[a-z0-9._+-]+', carpet_version):
		carpet_repository = 'https://maven.fallenbreath.me/jitpack'
		carpet_coordinate = carpet_version
	else:
		carpet_repository = 'https://masa.dy.fi/maven'
		carpet_coordinate = f'carpet:fabric-carpet:{carpet_version}'
	download_maven_jar(carpet_repository, carpet_coordinate, mods_dir / 'carpet.jar')
	# Keep the auditor version in sync with common.gradle.
	download_maven_jar('https://maven.fallenbreath.me/releases', 'me.fallenbreath:mixin-auditor:0.3.0-o', mods_dir / 'mixin-auditor.jar')

	write_outputs(
		java_version=java_version,
	)


def main():
	parser = argparse.ArgumentParser(description=__doc__)
	subparsers = parser.add_subparsers(dest='command', required=True)
	subparsers.add_parser('matrix', help='Generate a matrix from built mod jars')
	prepare_parser = subparsers.add_parser('prepare', help='Prepare a production Fabric server')
	prepare_parser.add_argument('--subproject', required=True)
	prepare_parser.add_argument('--jar', type=Path, required=True)
	args = parser.parse_args()
	if args.command == 'matrix':
		generate_matrix()
	else:
		prepare_server(args.subproject, args.jar)


if __name__ == '__main__':
	main()
