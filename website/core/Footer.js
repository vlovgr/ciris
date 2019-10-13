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
    return (
      <footer className="nav-footer" id="footer">
        <hr className="separator" />
        <section className="copyright">
          {this.props.config.copyright}
          <br />
          Icon designed by <a href="https://www.flaticon.com/authors/freepik" rel="noopener">Freepik</a> from Flaticon.
        </section>
      </footer>
    );
  }
}

module.exports = Footer;
