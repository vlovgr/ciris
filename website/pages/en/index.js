const React = require("react");

const CompLibrary = require("../../core/CompLibrary.js");

const variables = require(process.cwd() + "/variables.js");

const MarkdownBlock = CompLibrary.MarkdownBlock;
const Container = CompLibrary.Container;
const GridBlock = CompLibrary.GridBlock;

class HomeSplash extends React.Component {
  render() {
    const { siteConfig, language = "" } = this.props;
    const { baseUrl, docsUrl } = siteConfig;
    const docsPart = `${docsUrl ? `${docsUrl}/` : ""}`;
    const langPart = `${language ? `${language}/` : ""}`;
    const docUrl = (doc) => `${baseUrl}${docsPart}${langPart}${doc}`;

    const SplashContainer = (props) => (
      <div className="homeContainer">
        <div className="homeSplashFade">
          <div className="wrapper homeWrapper">{props.children}</div>
        </div>
      </div>
    );

    const ProjectTitle = () => (
      <h2 className="projectTitle">
        <span>
          <img className="projectTitleLogo" src={siteConfig.titleIcon} />
          {siteConfig.title}
        </span>
        <small>{siteConfig.tagline}</small>
      </h2>
    );

    const PromoSection = (props) => (
      <div className="section promoSection">
        <div className="promoRow">
          <div className="pluginRowBlock">{props.children}</div>
        </div>
      </div>
    );

    const Button = (props) => (
      <div className="pluginWrapper buttonWrapper">
        <a className="button" href={props.href} target={props.target}>
          {props.children}
        </a>
      </div>
    );

    return (
      <SplashContainer>
        <div className="inner">
          <ProjectTitle siteConfig={siteConfig} />
          <PromoSection>
            <Button href={siteConfig.apiUrl}>API Docs</Button>
            <Button href={docUrl("overview", language)}>Documentation</Button>
            <Button href={siteConfig.repoUrl}>View on GitHub</Button>
          </PromoSection>
        </div>
      </SplashContainer>
    );
  }
}

class Index extends React.Component {
  render() {
    const { config: siteConfig, language = "" } = this.props;
    const { baseUrl } = siteConfig;

    const {
      organization,
      coreModuleName,
      latestVersion,
      scalaPublishVersions,
      scalaJsMajorMinorVersion,
      scalaNativeMajorMinorVersion,
    } = variables;

    const latestVersionBadge = latestVersion
      .replace("-", "--")
      .replace("_", "__");

    const Block = (props) => (
      <Container
        padding={["bottom", "top"]}
        id={props.id}
        background={props.background}
      >
        <GridBlock
          align="center"
          contents={props.children}
          layout={props.layout}
        />
      </Container>
    );

    const index =
      `[![Typelevel](https://img.shields.io/badge/typelevel-library-fd3d50.svg)](https://typelevel.org/projects/#ciris) [![GitHub Actions](https://img.shields.io/github/actions/workflow/status/vlovgr/ciris/ci.yml?branch=master) [![Discord](https://img.shields.io/discord/632277896739946517?color=5865f2)](https://discord.gg/XF3CXcMzqD) [![Version](https://img.shields.io/badge/version-v${latestVersionBadge}-orange.svg)](https://index.scala-lang.org/vlovgr/ciris)

Functional, lightweight, and composable configuration loading for Scala.<br>
Project is under active development. Feedback and contributions welcome.

### Getting Started
To get started with [sbt](https://scala-sbt.org), add the following line to your \`build.sbt\` file.

\`\`\`scala
libraryDependencies += "${organization}" %% "${coreModuleName}" % "${latestVersion}"
\`\`\`

Published for Scala ${scalaPublishVersions}, [Scala.js](https://www.scala-js.org) ${scalaJsMajorMinorVersion} and [Scala Native](https://scala-native.org) ${scalaNativeMajorMinorVersion}.

For changes between versions, please refer to the [release notes](https://github.com/vlovgr/ciris/releases).
`.trim();

    return (
      <div>
        <HomeSplash siteConfig={siteConfig} language={language} />
        <div className="mainContainer">
          <div className="index">
            <MarkdownBlock>{index}</MarkdownBlock>
          </div>
        </div>
      </div>
    );
  }
}

module.exports = Index;
