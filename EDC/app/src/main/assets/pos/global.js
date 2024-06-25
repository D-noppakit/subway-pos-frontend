const env = "uat"
let endpoint = "http://localhost:3000"
if (env === "prd") {
    endpoint = "https://dinosaur-jump-83e560741e48.herokuapp.com"
}
function auth() {
    console.log("login : ", localStorage.IsLogin)
    if (localStorage.IsLogin == "false" || !localStorage.IsLogin || !localStorage.token) {
        window.location.href = "./login.html"
    }
}

function get_path() {
    return window.location.pathname
}
window.addEventListener('load', function () {
    if (window.location.pathname.includes("login.html")
        || window.location.pathname.includes("setting.html")
        || window.location.pathname.includes("order.html")) {
        console.log("pass")
        if (window.location.pathname.includes("order.html")) {
            auth()
        }
    } else {
        auth()
        get_order()
        sw_global()
        setInterval(() => {
            sw_global()
            get_order()
        }, 3000);
    }
});


function Loading(boo) {
    if (boo) {
        document.getElementById("loading").style.display = 'flex'
    } else {
        document.getElementById("loading").style.display = 'none'
    }

}

function formatDateThai() {
    const monthsThai = [
        "ม.ค.", "ก.พ.", "มี.ค.", "เม.ย.", "พ.ค.", "มิ.ย.",
        "ก.ค.", "ส.ค.", "ก.ย.", "ต.ค.", "พ.ย.", "ธ.ค."
    ];

    const today = new Date();
    const day = today.getDate();
    const month = monthsThai[today.getMonth()];
    const year = today.getFullYear();

    return `${day} ${month} ${year}`;
}

let tab1 = `` //ใหม่
let tab2 = `` //จัดเตรียม
let tab3 = `` // รอรับ
let tab4 = `` // ประวัติ
let start_order = []
let cook = []
let pickup_order = []
let history = []

function get_order() {
    tab1 = ``
    tab2 = ``
    tab3 = ``
    tab4 = ``
    if (!localStorage.token) {
        return
    }
    const myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    const raw = JSON.stringify({
        "token": localStorage.token
    });

    const requestOptions = {
        method: "POST",
        headers: myHeaders,
        body: raw,
        redirect: "follow"
    };


    fetch(endpoint + "/api/pos/get/order", requestOptions)
        .then((response) => response.json())
        .then((result) => {
            // console.log(result)
            start_order = result.result.filter((v) => v.status === "start-order")
            cook = result.result.filter((v) => v.status === "cook")
            pickup_order = result.result.filter((v) => v.status === "pickup-order")
            history = result.result.filter((v) => v.status === "complete" || v.status === "cancel")
            for (let index = 0; index < start_order.length; index++) {
                const element = start_order[index];
                let txid = element.txid.trim()
                tab1 += `
            <div onclick="go_to_order('${txid}'); "
                class="flex flex-col border border-1 rounded-2xl p-3 my-3">
                <div class="flex justify-between items-center ">
                    <div class="flex justify-center items-center">
                        <span class="text-[#008938]">
                           ${"SWC-" + element.id}
                        </span>
                        <span
                            class="bg-[#FF5C39] ms-1 h-[20px] w-[40px] flex justify-center items-center text-white rounded-full ">New</span>
                    </div>
                    <div>
                        ฿269
                    </div>
                </div>
                <div class="flex justify-between items-center text-[#FF5C39]">
                    <div class="flex justify-center items-center ">
                        <span class="text-[14px] font-[400]">
                            รับทันที
                        </span>
                    </div>
                    <div>
                        เวลา 12:15 น.
                    </div>
                </div>
            </div>

                `
            }
            for (let index = 0; index < cook.length; index++) {
                const element = cook[index];
                let txid = element.txid.trim()
                tab2 += `
                <div onclick="go_to_order('${txid}'); "
                class="flex flex-col border border-1 rounded-2xl p-3 my-3">
                <div class="flex justify-between items-center ">
                    <div class="flex justify-center items-center">
                        <span class="text-[#008938]">
                           ${"SWC-" + element.id + "Cook"}
                        </span>
                        <span
                            class="bg-[#FF5C39] ms-1 h-[20px] w-[40px] flex justify-center items-center text-white rounded-full ">New</span>
                    </div>
                    <div>
                        ฿269
                    </div>
                </div>
                <div class="flex justify-between items-center text-[#FF5C39]">
                    <div class="flex justify-center items-center ">
                        <span class="text-[14px] font-[400]">
                            รับทันที
                        </span>
                    </div>
                    <div>
                        เวลา 12:15 น.
                    </div>
                </div>
            </div>

                `
            }
            for (let index = 0; index < pickup_order.length; index++) {
                const element = pickup_order[index];
                let txid = element.txid.trim()
                tab3 += `
                <div onclick="go_to_order('${txid}'); "
                class="flex flex-col border border-1 rounded-2xl p-3 my-3">
                <div class="flex justify-between items-center ">
                    <div class="flex justify-center items-center">
                        <span class="text-[#008938]">
                           ${"SWC-" + element.id}
                        </span>
                        <span
                            class="bg-[#FF5C39] ms-1 h-[20px] w-[40px] flex justify-center items-center text-white rounded-full ">New</span>
                    </div>
                    <div>
                        ฿269
                    </div>
                </div>
                <div class="flex justify-between items-center text-[#FF5C39]">
                    <div class="flex justify-center items-center ">
                        <span class="text-[14px] font-[400]">
                            รับทันที
                        </span>
                    </div>
                    <div>
                        เวลา 12:15 น.
                    </div>
                </div>
            </div>

                `
            }
            for (let index = 0; index < history.length; index++) {
                const element = history[index];
                const { pickup_time, status , id } = element
                let txid = element.txid.trim()
                if (status === "cancel") {
                    tab4 += `
                    <div class="flex flex-col border border-1 rounded-2xl p-3 my-3">
                <div class="flex justify-between items-center ">
                    <div class="flex justify-center items-center">
                        <span class="text-[#008938]">
                        SW-${id}
                        </span>

                    </div>
                    <div>
                        ฿269
                    </div>
                </div>
                <div class="flex justify-between items-center text-[#E9AF3D] text-[12px]">
                    <div class="flex justify-center items-center ">
                        <span class="text-[12px] font-[400] text-[#FF5C39]">
                            ยกเลิกออเดอร์
                        </span>
                    </div>
                    <div class="text-[#FF5C39] font-[400] text-[12px] ">
                        เวลา 12:15 น.
                    </div>
                </div>
            </div>
                    `
                } else {
                    tab4 += `
                    <div onclick="go_to_order('${txid}');"
                    class="flex flex-col border border-1 rounded-2xl p-3 my-3">
                    <div class="flex justify-between items-center ">
                        <div class="flex justify-center items-center">
                            <span class="text-[#008938]">
                                SW-${id}
                            </span>
    
                        </div>
                        <div>
                            ฿269
                        </div>
                    </div>
                    <div class="flex justify-between items-center text-[#FF5C39]">
                        <div class="flex justify-center items-center ">
                            <span class="text-[12px] font-[400] text-[#A3A6B7]">
                                ${pickup_time === "-" ? "รับทันที" : " เวลารับ 12:30 น."}
                            </span>
                        </div>
                        <div>
    
                        </div>
                    </div>
                </div>
    
                    `
                }

            }
            setTab(localStorage.tab)
        })
        .catch((error) => {
            console.error(error)
            Loading(false)
        });
}



function setTab(n) {
    console.log("tab " + n)
    if (n === "1") {
        const listOrderElement = document.getElementById("list-order");
        if (listOrderElement.innerHTML !== tab1) {
            document.getElementById("list-order").innerHTML = tab1
            document.getElementById("length_index").innerHTML = start_order.length
        }
        if (tab1 == '') {
            document.getElementById("list-order").innerHTML = ` <div class="w-full justify-center items-center flex mt-10">
            <img class="object-contain w-[112px]" src="./imgs/no-item.png" alt="">
        </div>`
        }

    } else if ((n === "2")) {
        const listOrderElement = document.getElementById("list-order");
        if (listOrderElement.innerHTML !== tab2) {

            document.getElementById("list-order").innerHTML = tab2
            document.getElementById("length_index").innerHTML = cook.length
        }
        if (tab2 == '') {
            document.getElementById("list-order").innerHTML = ` <div class="w-full justify-center items-center flex mt-10">
            <img class="object-contain w-[112px]" src="./imgs/no-item.png" alt="">
        </div>`
        }
    }
    else if ((n === "3")) {
        const listOrderElement = document.getElementById("list-order");
        if (listOrderElement.innerHTML !== tab3) {
            document.getElementById("list-order").innerHTML = tab3
            document.getElementById("length_index").innerHTML = pickup_order.length
        }
        if (tab3 == '') {
            document.getElementById("list-order").innerHTML = ` <div class="w-full justify-center items-center flex mt-10">
            <img class="object-contain w-[112px]" src="./imgs/no-item.png" alt="">
        </div>`
        }

    }
    else if ((n === "4")) {
        const listOrderElement = document.getElementById("list-order");
        if (listOrderElement.innerHTML !== tab4) {
            document.getElementById("list-order").innerHTML = tab4
        }
        if (tab4 == '') {
            document.getElementById("list-order").innerHTML = ` <div class="w-full justify-center items-center flex mt-10">
            <img class="object-contain w-[112px]" src="./imgs/no-item.png" alt="">
        </div>`
        }

        document.getElementById("length_index").innerHTML = history.length
    }
    Loading(false)
}


function sw_global() {

    const myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    const raw = JSON.stringify({
        "token": localStorage.token
    });

    const requestOptions = {
        method: "POST",
        headers: myHeaders,
        body: raw,
        redirect: "follow"
    };

    fetch(endpoint + "/api/pos/alert-status", requestOptions)
        .then((response) => response.json())
        .then((result) => {
            Loading(false)
            console.log(result)
            if (result.msg === "good") {
                const { open_order, alert_noti } = result.result
                console.log({ open_order, alert_noti })

                if (open_order) {
                    document.getElementById("emergency-1").style.display = 'none'
                } else {
                    document.getElementById("emergency-1").style.display = 'block'
                }
                // document.getElementById("alert_noti").checked = alert_noti
            } else {

            }
        })
        .catch((error) => {
            Loading(false)
            console.error(error)
        });
}
function go_to_order(tx) {
    window.location.href = './order.html'
    localStorage.tx = tx
}
