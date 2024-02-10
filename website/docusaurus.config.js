// @ts-check
// `@type` JSDoc annotations allow editor autocompletion and type checking
// (when paired with `@ts-check`).
// There are various equivalent ways to declare your Docusaurus config.
// See: https://docusaurus.io/docs/api/docusaurus-config

import {themes as prismThemes} from 'prism-react-renderer';

const isVercel = process.env.VERCEL === '1'
let url, baseUrl, commitUrl, commitShaShort
if (isVercel) {
  url = `https://${process.env.VERCEL_URL}`
  baseUrl = '/'
  commitUrl = `https://github.com/${process.env.VERCEL_GIT_REPO_OWNER}/${process.env.VERCEL_GIT_REPO_SLUG}/commit/${process.env.VERCEL_GIT_COMMIT_SHA}`
  commitShaShort = process.env.VERCEL_GIT_COMMIT_SHA.substring(0, 7)
} else {
  url = 'https://tisunion.github.io'
  baseUrl = '/Carpet-TIS-Addition/'
}

/** @type {import('@docusaurus/types').Config} */
const config = {
  title: 'Carpet TIS Addition',
  tagline: 'A Carpet mod (fabric-carpet) extension, a collection of carpet mod style useful tools and interesting features',
  favicon: 'img/icon.png',

  // Set the production url of your site here
  url: url,
  // Set the /<baseUrl>/ pathname under which your site is served
  // For GitHub pages deployment, it is often '/<projectName>/'
  baseUrl: baseUrl,

  // GitHub pages deployment config.
  // If you aren't using GitHub pages, you don't need these.
  organizationName: 'TISUnion', // Usually your GitHub org/user name.
  projectName: 'Carpet-TIS-Addition', // Usually your repo name.
  trailingSlash: false,

  onBrokenLinks: 'throw',
  onBrokenMarkdownLinks: 'warn',

  // Even if you don't use internationalization, you can use this field to set
  // useful metadata like html lang. For example, if your site is Chinese, you
  // may want to replace "en" with "zh-Hans".
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
          sidebarPath: './sidebars.js',
          // Please change this to your repo.
          // Remove this to remove the "edit this page" links.
          editUrl:
            'https://github.com/TISUnion/Carpet-TIS-Addition/tree/master/website/',
        },
        blog: false,
        theme: {
          customCss: './src/css/custom.css',
        },
      }),
    ],
  ],

  themeConfig:
    /** @type {import('@docusaurus/preset-classic').ThemeConfig} */
    ({
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
            to: 'eol',
            position: 'left',
            label: 'EOL',
          },
          {
            type: 'search',
            position: 'right',
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
        ],
      },
      footer: {
        style: 'light',
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
        theme: prismThemes.github,
        darkTheme: prismThemes.dracula,
      },
      // https://docusaurus.io/docs/search#using-algolia-docsearch
      algolia: {
        appId: 'U1J6M8YHJ6',
        apiKey: 'd3602f9d3f867570f410fb63a08b46c5',
        indexName: 'carpet-tis-addition',
        contextualSearch: true,
      },
      announcementBar: isVercel ? {
        id: 'test_environment',
        content: `⚠️ This is the test environment of the Carpet TIS Addition website. Built for commit <a href="${commitUrl}">${commitShaShort}</a>. ⚠️`,
        backgroundColor: '#ffdf76',
        textColor: '#091E42',
        isCloseable: false,
      } : undefined,
    }),
};

export default config;
