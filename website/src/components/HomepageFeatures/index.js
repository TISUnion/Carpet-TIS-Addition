import React from 'react';
import clsx from 'clsx';
import styles from './styles.module.css';
import Translate, {translate} from '@docusaurus/Translate';

const FeatureList = [
  {
    title: translate({id: 'homepage.feature.rich.message', message: 'Rich Features & Tools'}),
    Svg: require('@site/static/img/toolbox.svg').default,
    description: (
      <>
        <Translate id="homepage.feature.rich.description">
          Carpet TIS Addition packs an array of features and tools,
          including carpet rules, loggers and commands,
          enriching your Technical Minecraft experience
        </Translate>
      </>
    ),
  },
  {
    title: translate({id: 'homepage.feature.vanilla.message', message: 'Keep the game vanilla'}),
    Svg: require('@site/static/img/vanilla.svg').default,
    description: (
      <>
        <Translate id="homepage.feature.vanilla.description">
          Carpet TIS Addition never changes the vanilla behavior unless explicitly configured by the user.
          Keep Minecraft vanilla
        </Translate>
      </>
    ),
  },
  {
    title: translate({id: 'homepage.feature.versions.message', message: 'Extensive Version Support'}),
    Svg: require('@site/static/img/checkmark.svg').default,
    description: (
      <>
        <Translate id="homepage.feature.versions.description">
          From Minecraft 1.14 to the latest snapshot,
          Carpet TIS Addition provides consistent support across multiple Minecraft versions
        </Translate>
      </>
    ),
  },
];

function Feature({Svg, title, description}) {
  return (
    <div className={clsx('col col--4')}>
      <div className="text--center">
        <Svg className={styles.featureSvg} role="img" />
      </div>
      <div className="text--center padding-horiz--md">
        <h3>{title}</h3>
        <p>{description}</p>
      </div>
    </div>
  );
}

export default function HomepageFeatures() {
  return (
    <section className={styles.features}>
      <div className="container">
        <div className="row">
          {FeatureList.map((props, idx) => (
            <Feature key={idx} {...props} />
          ))}
        </div>
      </div>
    </section>
  );
}
