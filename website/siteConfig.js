const repoUrl = "https://github.com/vlovgr/ciris";

const apiUrl = "/api/ciris/index.html";

// See https://docusaurus.io/docs/site-config for available options.
const siteConfig = {
  title: "Ciris",
  tagline: "Functional Configurations for Scala",
  url: "https://cir.is",
  baseUrl: "/",
  cname: "cir.is",

  customDocsPath: "docs/target/mdoc",

  projectName: "ciris",
  organizationName: "vlovgr",

  headerLinks: [
    { blog: true, label: "Blog" },
    { href: apiUrl, label: "API Docs" },
    { doc: "overview", label: "Documentation" },
    { href: repoUrl, label: "GitHub" },
  ],

  blogSidebarCount: "ALL",
  blogSidebarTitle: { default: "Recent Posts", all: "Blog Posts" },

  headerIcon: "img/ciris.white.svg",
  titleIcon: "img/ciris.svg",
  favicon: "img/favicon.png",

  colors: {
    primaryColor: "#122932",
    secondaryColor: "#153243",
  },

  copyright: `Copyright © 2017-${new Date().getFullYear()} Viktor Rudebeck.`,

  highlight: { theme: "github" },

  onPageNav: "separate",

  separateCss: ["api"],

  cleanUrl: true,

  repoUrl,

  apiUrl,
};

module.exports = siteConfig;
