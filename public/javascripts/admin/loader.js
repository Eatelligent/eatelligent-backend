/*
 * Script loader to be used while in development.
 **/
(function(window, document) {
  var injectCss = function(href, media) {
    var link = document.createElement("link");
    link.rel = "stylesheet";
    link.type = "text/css";
    link.href = href;

    // Temporarily set media to something "invalid" in order to fetch the css
    // without blocking rendering.
    link.media = "only x";

    // Find the first script tag on the page and insert the `link`-tag.
    var entry = document.getElementsByTagName("script")[0];
    entry.parentNode.insertBefore(link, entry);

    // Set media to `all`. Doing it within a `setTimeout`-callback causes it to
    // flush the current media value to the DOM.
    setTimeout(function() {
      link.media = media || "all";
    });
  };

  var injectScript = function(path, attrs) {
    var script = document.createElement("script");
    script.async = true;
    script.src = path;

    // Add attributes.
    for (var key in attrs) {
      if (attrs.hasOwnProperty(key)) {
        script.setAttribute(key, attrs[key]);
      }
    }

    // Find the first script tag on the page and insert the `script`-tag.
    var entry = document.getElementsByTagName("script")[0];
    entry.parentNode.insertBefore(script, entry);
  }
  var baseUrl = 'assets/javascripts/admin/app/';
  window.isProduction = window.location.hostname === 'localhost' ? false : true;

  injectScript("assets/javascripts/admin/bower_components/requirejs/require.js", {"data-main": baseUrl + "loader.js"});
  injectCss("assets/javascripts/admin/styles/main.css");
    
  // if (!window.isProduction) {
  //   injectScript("http://"+(location.host||"localhost").split(":")[0]+":35729/livereload.js?snipver=1")
  // }
})(window, document);
