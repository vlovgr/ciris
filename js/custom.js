// Cleanup footer credits
$('#site-footer .row:last p').html('Website built with <a href="https://47deg.github.io/sbt-microsites" target="_blank">sbt-microsites</a> by <a href="https://www.47deg.com" target="_blank">47 Degrees</a>')

// Linkify main page
jQuery(document).ready(function() {
  linkifyAllLevels("#site-main #content");
});

var anchorForId = function (id) {
  var anchor = document.createElement("a");
  anchor.className = "header-link";
  anchor.href      = "#" + id;
  anchor.innerHTML = "<i class=\"fa fa-link\"></i>";
  return anchor;
};

var linkifyAnchors = function (level, containingElement) {
  var headers = containingElement.getElementsByTagName("h" + level);
  for (var h = 0; h < headers.length; h++) {
    var header = headers[h];

    if (typeof header.id !== "undefined" && header.id !== "") {
      header.appendChild(anchorForId(header.id));
    }
  }
};

var linkifyAllLevels = function (blockSelector) {
  var contentBlock = document.querySelector(blockSelector);
  if (!contentBlock) {
    return;
  }
  for (var level = 1; level <= 4; level++) {
    linkifyAnchors(level, contentBlock);
  }
};
