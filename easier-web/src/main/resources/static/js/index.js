url = function (i) {
    // return "http://123.57.236.58:8080/easier/" + i;
    return "http://localhost:8080/easier/" + i;
};
//重获token
getToken = function () {
    var user = localStorage.getItem("user");
    if (null == user) {
        window.location.replace("login.html");
    }
    console.log("更新前用户:" + user);
    var username = JSON.parse(user).username;
    var password = JSON.parse(user).password;
    //ajax
    $.ajax({
        type: 'post',
        url: url("login"),
        cache: false,
        dataType: 'json',
        data: {
            username: username,
            password: password
        },
        beforeSend: function () {
        },
        success: function (data) {
            var res = data;
            switch (res.status) {
                //登陆成功
                case 200:
                    //将用户信息进行本地存储
                    var storage = window.localStorage;
                    //设置用户信息到本地
                    storage.removeItem("user");
                    storage.setItem("user", JSON.stringify(res.obj.user));
                    var token = res.obj.token;
                    storage.setItem("token", token);
                    window.location.reload();
                    break;
                case 500:
                    console.log("出错啦");
                    break;
            }
        }
    });
};
//绑定提示框样式
$(document).ready(function () {
    $('[data-toggle="tooltip"]').tooltip();
});

//获取格式化时间
function fmtDate(data, format) {
    var year = data.getFullYear(); //获取年
    var month = data.getMonth() + 1;
    month = month.toString().length == 1 ? ("0" + month) : month; //获取月
    var day = data.getDate();
    day = day.toString().length == 1 ? ("0" + day) : day; //获取日
    var hours = data.getHours();
    hours = hours.toString().length == 1 ? ("0" + hours) : hours;
    var minutes = data.getMinutes();
    minutes = minutes.toString().length == 1 ? ("0" + minutes) : minutes;
    var seconds = data.getSeconds();
    seconds = seconds.toString().length == 1 ? ("0" + seconds) : seconds;
    switch (format) {
        case "date":
            return year + "/" + month + "/" + day;
        case "time":
            return hours + ":" + minutes + ":" + seconds;
        case "datetime":
            return year + "/" + month + "/" + day + " " + hours + ":" + minutes + ":" + seconds;
    }
    time = year + "/" + month + "/" + day + " " + hours + ":" + minutes + ":" + seconds;
    return time;
}

//获取时间差
function getDateDiff(data) {
    var dateTimeStamp = Date.parse(data);
    var minute = 1000 * 60;
    var hour = minute * 60;
    var day = hour * 24;
    var halfamonth = day * 15;
    var month = day * 30;
    var now = new Date().getTime();
    var diffValue = now - dateTimeStamp;
    if (diffValue < 0) {
        //若日期不符则弹窗口告之,结束日期不能小于开始日期！
    }
    var monthC = diffValue / month;
    var weekC = diffValue / (7 * day);
    var dayC = diffValue / day;
    var hourC = diffValue / hour;
    var minC = diffValue / minute;
    if (dayC > 7) {
        result = fmtDate(new Date(data), "date")
    } else if (dayC >= 7) {
        result = parseInt(weekC) + "周前";
    } else if (dayC >= 1) {
        result = parseInt(dayC) + "天前";
    } else if (hourC >= 1) {
        result = parseInt(hourC) + "小时前";
    } else if (minC >= 1) {
        result = parseInt(minC) + "分钟前";
    } else
        result = "刚刚";
    return result;
}

$(function () {
    //加载用户信息
    var user = JSON.parse(localStorage.getItem("user"));
    if (user != null) {
        $("#user-bar").removeClass("d-none");

        $("#user-bar span").text(user.nickName);

        $("#user-bar img").attr("src", user.headPic);

        $("#mine").removeClass("d-none");
    } else {
        $("#login-bar").removeClass("d-none");
    }
    //绑定Blog,未登录不允许发布
    $("#blog").click(function () {
        if (null == localStorage.getItem("user")) {
            swal({
                title: "尚未登陆",
                text: "正在前往登录页...",
                icon: "error",
                buttons: false,
                timer: 1500,
            }).then(function () {
                window.location.replace("login.html")
            });
        } else {
            window.location.replace("release.html")
        }
    });
    //登出
    $("#logout").click(function () {
        var user = localStorage.getItem("user");
        if (null != user) {
            swal({
                title: "确定要退出登录么?",
                text: "  ",
                icon: "warning",
                buttons: {
                    cancel: "取消",
                    confirm: "退出",
                },
                dangerMode: true,
            }).then((willDelete)=> {
                if(willDelete) {
                    //todo 调用logout接口
                    localStorage.removeItem("user");
                    localStorage.removeItem("token");
                    swal({
                        title: "退出成功",
                        text: " ",
                        icon: "success",
                        buttons: false,
                        timer: 1000,
                    }).then(function () {
                        window.location.replace("/easier/home.html")
                    });
                }
            }
        )
        }
    });
});
