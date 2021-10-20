function logout_handler() {
    window.location.href= "/logout"
}

function URL_add_parameter(url, param, value) {
    var hash = {};
    var parser = document.createElement('a');
    parser.href = url;
    var parameters = parser.search.split(/\?|&/);
    for (var i = 0; i < parameters.length; i++) {
        if (!parameters[i])
            continue;
        var ary = parameters[i].split('=');
        hash[ary[0]] = ary[1];
    }
    hash[param] = value;
    var list = [];
    Object.keys(hash).forEach(function (key) {
        list.push(key + '=' + hash[key]);
    });
    parser.search = '?' + list.join('&');
    return parser.href;
}

$(document).ready(function () {
    $(".dropdown-item ").click(function (e) {
        // e.preventDefault()
        let id = e.currentTarget.getAttribute("id");
        if (id.startsWith("lang")) {
            let lang_code = e.currentTarget.getAttribute("value");
            window.location.href = URL_add_parameter(location.href, 'lang', lang_code);
        }
    });
});
