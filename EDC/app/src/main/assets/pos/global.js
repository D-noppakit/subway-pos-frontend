function auth() {
    console.log("login : ", localStorage.IsLogin)
    if (localStorage.IsLogin == "false" || !localStorage.IsLogin) {
        window.location.href = "./login.html"
    }
}
auth()

function Loading(boo) {
    if (boo) {
        document.getElementById("loading").style.display = 'flex'
    } else {
        document.getElementById("loading").style.display = 'none'
    }

}

