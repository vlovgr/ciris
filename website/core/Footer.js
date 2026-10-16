const React = require("react");

class Footer extends React.Component {
  docUrl(doc, language) {
    const baseUrl = this.props.config.baseUrl;
    const docsUrl = this.props.config.docsUrl;
    const docsPart = `${docsUrl ? `${docsUrl}/` : ""}`;
    const langPart = `${language ? `${language}/` : ""}`;
    return `${baseUrl}${docsPart}${langPart}${doc}`;
  }

  pageUrl(doc, language) {
    const baseUrl = this.props.config.baseUrl;
    return baseUrl + (language ? `${language}/` : "") + doc;
  }

  render() {
    const repoUrl = this.props.config.repoUrl;
    return (
      <footer className="nav-footer" id="footer">
        <hr className="separator" />
        <section className="copyright">
          Licensed under the <a href={`${repoUrl}/blob/main/license.txt`}>MIT License</a>.
          <br />
          {this.props.config.copyright}
        </section>
      </footer>
    );
  }
}

module.exports = Footer;
