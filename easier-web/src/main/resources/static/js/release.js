$(function () {
	$("#nav-list li:eq(1) a").addClass("nav-active");

	$("#title span").text("发布Blog");
	$("#title svg use").attr("xlink:href", "#icon-biji");

	//判断是否登录
	if (null == localStorage.getItem("user")) {
		swal({
			title: "尚未登陆",
			text: "正在前往登录页...",
			icon: "error",
			buttons: false,
			timer: 1200,
		}).then(function () {
			window.location.replace("login.html")
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
			window.location.replace("release.html")
		}
	}
	//发布事件
	$("#releastBtn").click(function () {
		//校验标题&内容
		var title = $("#title-content").val();
		var safeTitle = filterXSS(title);
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
				topic: safeTitle,
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
							window.location.replace("/easier/index.html")
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
});