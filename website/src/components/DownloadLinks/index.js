import React from 'react';
import styles from './styles.module.css';
import Translate from '@docusaurus/Translate';

const DownloadWebsites = [
  {
    name: 'GitHub',
    Svg: require('@site/static/img/github.svg').default,
    url: "https://github.com/TISUnion/Carpet-TIS-Addition/releases",
    color: '#24292F',
  },
  {
    name: 'CurseForge',
    Svg: require('@site/static/img/curseforge.svg').default,
    url: "https://legacy.curseforge.com/minecraft/mc-mods/carpet-tis-addition/files",
    color: '#F16436',
  },
  {
    name: 'Modrinth',
    Svg: require('@site/static/img/modrinth.svg').default,
    url: "https://modrinth.com/mod/carpet-tis-addition/versions",
    color: '#00AF5C',
  },
]

function DownloadLink({name, Svg, url, color}) {
  return (
    <>
      <a href={url} className={styles.brandContainer} style={{background: color}}>
        <Svg className={styles.downloadSvg}/>
        <p>{name}</p>
      </a>
    </>
  );
}

export default function DownloadLinks() {
  return (
    <>
      <section className={styles.brandsContainer}>
        <h2 className="hero__subtitle text--center">
          <Translate id="homepage.downloads.title">
            Download Mod
          </Translate>
        </h2>
        <div>
          {DownloadWebsites.map((props, idx) => (
            <DownloadLink key={idx} {...props} />
          ))}
        </div>
      </section>
    </>
  );
}