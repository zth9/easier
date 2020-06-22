<%@ Page Title="" Language="C#" MasterPageFile="index.Master" AutoEventWireup="true" CodeBehind="release.aspx.cs" Inherits="esayer.views.WebForm5" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
    <script src="../js/wangEditor.min.js"></script>
    <script src="../js/xss.min.js"></script>
    <style type="text/css">
        #release-content {
            padding-top: 20px;
        }

        #title-text {
            border: 1px solid #ced4da;
        }
    </style>
    <script type="text/javascript">
        $(function () {
            //判断是否登录
            if (null == localStorage.getItem("user")) {
                swal({
                    title: "尚未登陆",
                    text: "正在前往登录页...",
                    icon: "error",
                    buttons: false,
                    timer: 1500,
                }).then(function () {
                    window.location.replace("login.aspx")
                });
            }

            //生成编辑框
            var E = window.wangEditor;
            var editor = new E('#edit-content');
            editor.customConfig.zIndex = 100;
            editor.create();

            //判断是否编辑
            var urlStr = window.location.search;
            var blogId = urlStr.substr(1);
            var editBlogId = null;
            //匹配正则 数字
            var formRule = new RegExp(/^[0-9]{1,16}$/);
            if ("" != urlStr) {
                if (blogId.match(formRule)) {
                    //验证授权
                    $.ajax({
                        type: 'get',
                        url: url("blog/edit/" + blogId),
                        cache: false,
                        dataType: 'json',
                        beforeSend: function (req) {
                            //设置token
                            req.setRequestHeader("token", localStorage.getItem("token"));
                            $('#loadmodal').modal('show');
                        },
                        success: function (data) {
                            var res = data;
                            switch (res.status) {
                                case 200:
                                    //设置blogid
                                    editBlogId = blogId;
                                    //设置标题
                                    $("#title-content").val(res.obj.topic);
                                    //设置正文
                                    editor.txt.html(res.obj.content);
                                    break;
                                case 500:
                                    switch (res.msg) {
                                        //token失效
                                        case "tokenError":
                                            getToken();
                                            break;
                                        //请求驳回
                                        case "reject":

                                            break;
                                        default:
                                            swal({
                                                title: res.msg,
                                                text: " ",
                                                icon: "error",
                                                buttons: true,
                                            });
                                    }
                                    break;
                            }

                        },
                        complete: function () {
                            console.log("执行完成");

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
                    window.location.replace("release.aspx")
                }
            }
            //发布事件
            $("#releastBtn").click(function () {
                console.log("开始发布");
                //校验标题&内容
                var title = $("#title-content").val();
                if (title == "") {
                    console.log("标题不能为空");
                    swal({
                        title: "标题不能为空",
                        text: " ",
                        icon: "warning",
                        buttons: false,
                        timer: 1000
                    });
                    return;
                }
                //var content = JSON.stringify(editor.txt.getJSON());
                if (!editor.txt.text().replace(/&nbsp;/g, '').trim()) {
                    if (editor.txt.html().indexOf('<img') == -1 && editor.txt.html().indexOf('<iframe') == -1) {
                        swal({
                            title: "内容不能为空",
                            text: " ",
                            icon: "warning",
                            buttons: false,
                            timer: 1000
                        });
                        return;
                    }
                }
                var filterHtml = editor.txt.html();
                var content = filterXSS(filterHtml);  // 此处进行 xss 攻击过滤
                //ajax发送blog
                $.ajax({
                    type: 'post',
                    url: url("blog"),
                    cache: false,
                    dataType: 'json',
                    data: {
                        blogId: editBlogId,
                        userId: JSON.parse(localStorage.getItem("user")).userId,
                        topic: title,
                        content: content
                    },
                    beforeSend: function (req) {
                        //设置token
                        req.setRequestHeader("token", localStorage.getItem("token"));
                        $('#loadmodal').modal('show');
                    },
                    success: function (data) {
                        var res = data;
                        switch (res.status) {
                            //发布成功
                            case 200:
                                swal({
                                    title: res.msg,
                                    text: "正在前往主页...",
                                    icon: "success",
                                    buttons: false,
                                    timer: 800,
                                });
                                setInterval(function () {
                                    window.location.replace("home.aspx")
                                }, 800);
                                break;
                            case 500:
                                swal("失败", res.msg, "error");
                                break;
                        }

                    },
                    complete: function () {
                        console.log("执行完成");
                        $("#ContentPlaceHolder1_submit").attr('disabled', false);

                        //完美关闭模态框
                        $('#loadmodal').modal('hide');
                        $('#loadmodal').on('shown.bs.modal', function () {
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
            });
        })
    </script>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
</asp:Content>
<asp:Content ID="Content3" ContentPlaceHolderID="ContentPlaceHolder2" runat="server">
    <div id="release-content" class="border-1">
        <div id="blog-title" class="d-flex flex-nowrap">
            <label id="title-text" class="btn text-nowrap" data-toggle="tooltip" data-placement="bottom" title="必须输入标题">标题</label>
            <input id="title-content" autocomplete="off" type="text" class="border-1 form-control text-dark text-center" placeholder="请输入标题">
        </div>
        <!-- 编辑框 -->
        <div id="edit-content">
        </div>
        <!-- 分栏&标签 -->
        <!-- 发布 -->
        <div id="releastBtn" class="d-flex justify-content-center m-2">
            <a class="btn text-dark btn-block" style="background-color:#ccc">
                <svg class="icon" aria-hidden="true">
                    <use xlink:href="#icon-fabu"></use>
                </svg>
                发布
            </a>
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
    </div>
    <script type="text/javascript">
        //标注当前页
        $("#nav-list li:eq(1) a").addClass("nav-active");
        //用来显示手机版 图标
        $("#title span").text("发布博客");
        $("#title svg use").attr("xlink:href", "#icon-biji");
    </script>
</asp:Content>
