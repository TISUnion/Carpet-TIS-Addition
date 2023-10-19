// @ts-check
// Note: type annotations allow type checking and IDEs autocompletion

const lightCodeTheme = require('prism-react-renderer/themes/github');
const darkCodeTheme = require('prism-react-renderer/themes/dracula');

/** @type {import('@docusaurus/types').Config} */
const config = {
  title: 'Carpet TIS Addition',
  tagline: 'A Carpet mod (fabric-carpet) extension, a collection of carpet mod style useful tools and interesting features',
  favicon: 'img/icon.png',

  // Set the production url of your site here
  url: 'https://tisunion.github.io',
  // Set the /<baseUrl>/ pathname under which your site is served
  // For GitHub pages deployment, it is often '/<projectName>/'
  baseUrl: '/Carpet-TIS-Addition/',

  // GitHub pages deployment config.
  // If you aren't using GitHub pages, you don't need these.
  organizationName: 'TISUnion', // Usually your GitHub org/user name.
  projectName: 'Carpet-TIS-Addition', // Usually your repo name.
  trailingSlash: false,

  onBrokenLinks: 'throw',
  onBrokenMarkdownLinks: 'warn',

  // Even if you don't use internalization, you can use this field to set useful
  // metadata like html lang. For example, if your site is Chinese, you may want
  // to replace "en" with "zh-Hans".
  i18n: {
    defaultLocale: 'en',
    locales: ['en', 'zh-Hans'],
  },

  presets: [
    [
      'classic',
      /** @type {import('@docusaurus/preset-classic').Options} */
      ({
        docs: {
          sidebarPath: require.resolve('./sidebars.js'),
          // Please change this to your repo.
          // Remove this to remove the "edit this page" links.
          editUrl:
            'https://github.com/TISUnion/Carpet-TIS-Addition/tree/master/website/',
        },
        blog: false,
        theme: {
          customCss: require.resolve('./src/css/custom.css'),
        },
      }),
    ],
  ],

  themeConfig:
    /** @type {import('@docusaurus/preset-classic').ThemeConfig} */
    ({
      // Replace with your project's social card
      image: 'img/docusaurus-social-card.jpg',
      navbar: {
        title: 'Carpet TIS Addition',
        logo: {
          alt: 'Carpet TIS Addition Icon',
          src: 'img/icon.png',
        },
        items: [
          {
            type: 'docSidebar',
            sidebarId: 'tutorialSidebar',
            position: 'left',
            label: 'Documents',
          },
          {
            type: 'localeDropdown',
            position: 'right',
          },
          {
            href: 'https://github.com/TISUnion/Carpet-TIS-Addition',
            className: 'header-github-link',
            'aria-label': 'GitHub repository',
            position: 'right',
          },
          // {
          //   type: 'search',
          //   position: 'right',
          // },
        ],
      },
      footer: {
        style: 'dark',
        links: [
          {
            title: 'Download',
            items: [
              {
                label: 'GitHub Releases',
                href: 'https://github.com/TISUnion/Carpet-TIS-Addition/releases',
              },
              {
                label: 'CurseForge',
                href: 'https://legacy.curseforge.com/minecraft/mc-mods/carpet-tis-addition/files',
              },
              {
                label: 'Modrinth',
                href: 'https://modrinth.com/mod/carpet-tis-addition/versions',
              },
            ],
          },
          {
            title: 'Contact',
            items: [
              {
                label: 'GitHub Repository',
                href: 'https://github.com/TISUnion/Carpet-TIS-Addition',
              },
              {
                label: 'Carpet Mod Discord',
                href: 'https://discord.gg/gn99m4QRY4',
              },
            ],
          },
          {
            title: 'Related Projects',
            items: [
              {
                label: 'TIS Carpet for 1.13.2',
                href: 'https://github.com/TISUnion/TISCarpet113',
              },
              {
                label: 'Fabric Carpet',
                href: 'https://github.com/gnembon/fabric-carpet',
              },
            ],
          },
        ],
        copyright: `Copyright © ${new Date().getFullYear()} Carpet TIS Addition. Built with Docusaurus.`,
      },
      prism: {
        theme: lightCodeTheme,
        darkTheme: darkCodeTheme,
      },
    }),
};

module.exports = config;
