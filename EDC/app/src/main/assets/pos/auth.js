function auth() {
    console.log("login : ", localStorage.IsLogin)
    if (localStorage.IsLogin == "false" || !localStorage.IsLogin) {
        window.location.href = "./login.html"
    }
}
auth()
