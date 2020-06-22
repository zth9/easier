<%@ Page Title="" Language="C#" MasterPageFile="login.Master" AutoEventWireup="true" CodeBehind="login.aspx.cs" Inherits="esayer.WebForm1" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
    <style type="text/css">
        #login-content {
            padding: 0;
            justify-content: center;
        }

        #login-form {
            min-width: 430px;
            background-color: white;
            border-radius: 10px;
            padding-top: 15px;
            padding-bottom: 10px;
            padding: 15px 10px 10px 10px;
        }

    </style>
    <script type="text/javascript">
        //判断已经登陆过时,直接跳转到主页
        if (null != localStorage.getItem("user")) {
            window.location.replace("home.aspx");
        }
        $(function () {


            //跳转到注册页
            $("#goregister").click(function () {
                $(window).attr('location', "signup.aspx");
            });

            //登录
            $(":submit").click(function () {
                //校验用户名和密码
                console.log("开始登陆");
                var username = $(":text").val();
                var password = $(":password").val();

                var formRule = new RegExp(/^[a-zA-Z0-9]{6,16}$/);
                //满足条件开始登录
                if (username.match(formRule) && password.match(formRule)) {
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
                            //禁止重复登陆
                            $(":submit").attr('disabled', true);
                            $('#loadmodal').modal('show');
                        },
                        success: function (data) {
                            var res = data;
                            switch (res.status) {
                                //登陆成功
                                case 200:
                                    $(":text").val("");
                                    $(":password").val("");
                                    //将用户信息进行本地存储
                                    var storage = window.localStorage;

                                    //设置用户信息到本地
                                    storage.removeItem("user");
                                    storage.setItem("user", JSON.stringify(res.obj.user));

                                    var token = res.obj.token;
                                    storage.setItem("token", token);

                                    swal({
                                        title: "登陆成功",
                                        text: "正在前往主页...",
                                        icon: "success",
                                        buttons: false,
                                        timer: 1000,
                                    }).then(function () {
                                        window.location.replace("home.aspx")
                                    });
                                    break;
                                case 500:
                                    swal("登录失败", res.msg, "error");
                                    break;
                            }

                        },
                        complete: function () {
                            console.log("执行完成");
                            $(":submit").attr('disabled', false);
                            //完美关闭模态框
                            $('#loadmodal').modal('hide');
                            $('#loadmodal').on('shown.bs.modal', function () {
                                console.log("完全可见");
                                $('#loadmodal').modal('hide');
                            });
                        },
                        error: function () {
                            swal({
                                title: "服务器繁忙",
                                text: "请稍后重试",
                                icon: "error",
                            });
                        }
                    });
                } else {
                    swal({
                        title: "非法输入",
                        text: "用户名以及密码应为6~16位 不允许特殊字符",
                        icon: "warning",
                    });
                }

            })
        });
    </script>
</asp:Content>
<asp:Content ID="Content3" ContentPlaceHolderID="ContentPlaceHolder2" runat="server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">

    <div id="login-content" class="row">
        <div id="login-form" class="col-md-4">
            <h4 class="text-center">
                <svg class="icon" aria-hidden="true">
                    <use xlink:href="#icon-denglu2"></use>
                </svg>
                <svg class="icon" aria-hidden="true">
                    <use xlink:href="#icon-denglu1"></use>
                </svg>
            </h4>
            <div class="input-group mb-3">
                <div class="input-group-prepend">
                    <span class="input-group-text">
                        <svg class="icon" aria-hidden="true">
                            <use xlink:href="#icon-denglu"></use>
                        </svg>
                    </span>
                </div>
                <asp:TextBox ID="username" runat="server" CssClass="form-control" placeholder="用户名"></asp:TextBox>

            </div>
            <div class="input-group mb-2">
                <asp:RequiredFieldValidator CssClass="bg-transparent text-danger " ID="usernameValidator" runat="server" ErrorMessage="" ControlToValidate="username" Display="Dynamic">用户名不能为空</asp:RequiredFieldValidator>
                <asp:RegularExpressionValidator CssClass="bg-transparent text-danger" ID="usernameRegValidator" runat="server" ErrorMessage="用户名仅支持6~16字母 数字 下划线" ControlToValidate="username" ValidationExpression="^[a-zA-Z0-9_]{6,16}$" Display="Dynamic"></asp:RegularExpressionValidator>
            </div>
            <div class="input-group mb-3">
                <div class="input-group-prepend">
                    <span class="input-group-text">
                        <svg class="icon" aria-hidden="true">
                            <use xlink:href="#icon-denglumima"></use>
                        </svg>
                    </span>
                </div>
                <asp:TextBox ID="password" runat="server" CssClass="form-control" placeholder="密码" TextMode="Password"></asp:TextBox>
            </div>
            <div class="input-group mb-2">
                <asp:RequiredFieldValidator CssClass="bg-transparent text-danger " ID="passwordValidator" runat="server" ErrorMessage="" ControlToValidate="password" Display="Dynamic">密码不能为空</asp:RequiredFieldValidator>
                <asp:RegularExpressionValidator CssClass="bg-transparent text-danger" ID="passwordRegValidator" runat="server" ErrorMessage="密码仅支持6~16字母 数字 下划线" ControlToValidate="password" ValidationExpression="^[a-zA-Z0-9_]{6,16}$" Display="Dynamic"></asp:RegularExpressionValidator>
            </div>
            <div class="input-group">
                <asp:Button ID="submit" class="btn btn-primary btn-block text-white" OnClientClick="return false" runat="server" Text="登录" />

                <div class="modal fade" id="loadmodal">
                    <div class="modal-dialog">
                        <div class="modal-content bg-loader">
                            <div class="modal-body d-flex justify-content-center align-items-center flex-column">
                                <div class="loader"></div>
                                <div class="text-white">世界上最远的距离是前端和后端</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="input-group">
                <div class="ml-auto">
                    <button id="goregister" type="button" class="btn btn-link btn-sm">还没有账号?注册一个!</button>
                </div>
            </div>
            <div class="input-group mb-3 row">
                <div class="col-5">
                    <button type="button" class="btn btn-link  btn-sm disabled">其他登陆方式</button>
                </div>
                <div class="col-7">
                    <div class="btn-group btn-group-sm">
                        <button type="button" class="btn btn-sm">
                            <svg class="icon" aria-hidden="true">
                                <use xlink:href="#icon-QQ"></use>
                            </svg>
                            QQ
                        </button>
                        <button type="button" class="btn btn-sm">
                            <svg class="icon" aria-hidden="true">
                                <use xlink:href="#icon-weixin"></use>
                            </svg>
                            微信
                        </button>
                        <button type="button" class="btn btn-sm">
                            <svg class="icon" aria-hidden="true">
                                <use xlink:href="#icon-weibo"></use>
                            </svg>
                            微博
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</asp:Content>
