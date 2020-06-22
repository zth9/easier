<%@ Page Title="" Language="C#" MasterPageFile="/login.Master" AutoEventWireup="true" CodeBehind="signup.aspx.cs" Inherits="esayer.views.WebForm1" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
    <style type="text/css">
        #register-content {
            padding: 0;
            justify-content: center;
        }

        #register-form {
            min-width: 430px;
            background-color: white;
            border-radius: 10px;
            padding-top: 15px;
            padding-bottom: 10px;
            padding: 15px 10px 10px 10px;
        }
    </style>
    <script type="text/javascript">
        $(function () {
            $("#gologin").click(function () {
                $(window).attr('location', "login.aspx");
            });

            //注册
            $(":submit").click(function () {
                //校验用户名和密码
                console.log("开始注册");
                var username = $(":text").val();
                var password = $(":password").eq(0).val();
                var passwordAgain = $(":password").eq(1).val();
                var formRule = new RegExp(/^[a-zA-Z0-9]{6,16}$/);
                //满足条件开始登录
                if (password != passwordAgain) {
                    swal({
                        title: "两次输入密码不一致",
                        text: " ",
                        icon: "warning",
                    });
                    return;
                }
                if (username.match(formRule) && password.match(formRule) && passwordAgain.match(formRule)) {
                    //清空
                    //$("#ContentPlaceHolder1_username").val("");
                    //$("#ContentPlaceHolder1_password").val("");

                    //ajax
                    $.ajax({
                        type: 'post',
                        url: url("user"),
                        cache: false,
                        dataType: 'json',
                        data: {
                            username: username,
                            password: password
                        },
                        beforeSend: function () {
                            //禁止重复注册
                            $("#ContentPlaceHolder1_submit").attr('disabled', true);
                            $('#loadmodal').modal('show');
                        },
                        success: function (data) {
                            var res = data;
                            switch (res.status) {
                                //登陆成功
                                case 200:
                                    //将用户信息保存至本地
                                    $("#ContentPlaceHolder1_username").val("");
                                    $("#ContentPlaceHolder1_password").val("");
                                    localStorage.removeItem("user");
                                    localStorage.setItem("user", JSON.stringify(res.obj));
                                    console.log("已经注册的对象:" + localStorage.getItem("user"));
                                    swal({
                                        title: "注册成功",
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
                            $("#ContentPlaceHolder1_submit").attr('disabled', false);

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
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder2" runat="server">
</asp:Content>
<asp:Content ID="Content3" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
    
    <div id="register-content" class="row">
        
        <div id="register-form" class="col-md-4">
            <h4 class="text-center">
                <svg class="icon" aria-hidden="true">
                    <use xlink:href="#icon-zhuce"></use>
                </svg>
                <svg class="icon" aria-hidden="true">
                    <use xlink:href="#icon-zhuce1"></use>
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
                <asp:RequiredFieldValidator CssClass="bg-transparent text-danger" ID="usernameValidator" runat="server" ErrorMessage="" ControlToValidate="username" Display="Dynamic">用户名不能为空</asp:RequiredFieldValidator>
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
            <div class="input-group mb-3">
                <div class="input-group-prepend">
                    <span class="input-group-text">
                        <svg class="icon" aria-hidden="true">
                            <use xlink:href="#icon-zhongzhimima"></use>
                        </svg>
                    </span>
                </div>
                <asp:TextBox ID="passwordAgain" runat="server" CssClass="form-control" placeholder="重复密码" TextMode="Password"></asp:TextBox>
            </div>
            <div class="input-group mb-2">
                <asp:RequiredFieldValidator CssClass="bg-transparent text-danger" ID="passwordAgainValidator" runat="server" ErrorMessage="" ControlToValidate="passwordAgain" Display="Dynamic">密码不能为空</asp:RequiredFieldValidator>
                <asp:CompareValidator CssClass="bg-transparent text-danger" ID="passwordAgainCompareValidator" runat="server" ErrorMessage="" ControlToValidate="passwordAgain" Display="Dynamic" ControlToCompare="password">两次密码输入不一致</asp:CompareValidator>
            </div>
            <div class="input-group">
                <asp:Button ID="submit" class="btn btn-primary btn-block text-white" OnClientClick="return false"  runat="server" Text="注册" />
                <div class="modal fade" id="loadmodal">
                    <div class="modal-dialog">
                        <div class="modal-content bg-loader">
                            <div class="modal-body d-flex justify-content-center align-items-center flex-column">
                                <div class="loader"></div>
                                <div class="text-white">世界上最远的距离是知道和做到</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="input-group">
                <div class="ml-auto">
                    <button id="gologin" type="button" class="btn btn-link btn-sm">已有账号?去登陆!</button>
                </div>
            </div>

        </div>
    </div>
</asp:Content>
