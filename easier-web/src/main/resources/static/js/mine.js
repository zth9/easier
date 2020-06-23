$(function() {
	$("#nav-list li:eq(2) a").addClass("nav-active");

	$("#title span").text("个人中心");
	$("#title svg use").attr("xlink:href", "#icon-gerenzhongxin");

	//显示页面内标签
	var urlStr = window.location.search;
	var tabStr = urlStr.substr(1);
	switch (tabStr) {
		case "edit":
			$("#edit-user").addClass('active');
			$("#edit").addClass('show');
			$("#edit").addClass('active');
			break;
		case "adv":
			$("#adv-info").addClass('active');
			$("#adv").addClass('show');
			$("#adv").addClass('active');
			break;
		default:
			$("#blog-manage").addClass('active');
			$("#blog-m").addClass('show');
			$("#blog-m").addClass('active');
	}
	//从localStroage加载用户信息
	var user = JSON.parse(localStorage.getItem("user"));
	$("#user-headPic").attr("src", user.headPic);
	$("#user-username").attr("placeholder", user.username);
	$("#user-nickName").attr("placeholder", user.nickName);
	if (user.email != null && user.email != "") {
		$("#user-email").attr("placeholder", user.email);
	} else {
		$("#user-email").attr("placeholder", "未绑定");
		$("#bind-email").append('<a id="bind" class="btn btn-primary" href="#">绑定邮箱</a>');
	}
	//更改头像
	$("#changePic").click(function() {
		$("choiceFile").click
	});

	function getObjectURL(file) {
		var url = null;
		if (window.createObjectURL != undefined) { // basic
			url = window.createObjectURL(file);
		} else if (window.URL != undefined) { // mozilla(firefox)
			url = window.URL.createObjectURL(file);
		} else if (window.webkitURL != undefined) { // webkit or chrome
			url = window.webkitURL.createObjectURL(file);
		}
		return url;
	}
	//上传头像到服务器
	function upimg() {
		var pic = $('#upload')[0].files[0];
		var file = new FormData();
		file.append('avatar', pic);
		$.ajax({
			url: url("mine/avatar"),
			type: "post",
			data: file,
			cache: false,
			contentType: false,
			processData: false,
			beforeSend: function(req) {
				//设置token
				req.setRequestHeader("token", localStorage.getItem("token"));
			},
			success: function(res) {

			}
		});
	}
	//更改密码
	$("#changePwd").click(function() {
		swal({
			text: "请输入旧密码",
			content: "input",
		}).then((input) => {
			if (input != JSON.parse(localStorage.getItem("user")).password;) {
				swal({
					title: "输入错误",
					text: " ",
					icon: "warning",
					buttons: false,
					timer: 500,
				});
			} else {
				swal({
					text: "请输入新密码",
					content: "input",
				}).then((input) => {
					if (;!input.replace(/&nbsp;/g, '').trim();) {
						swal({
							title: "输入不能为空",
							text: " ",
							icon: "warning",
							buttons: false,
							timer: 500,
						});
						return;
					}
					var formRule = new RegExp(/^[a-zA-Z0-9]{6,16}$/);
					//满足条件开始登录
					if (!input.match(formRule)) {
						swal({
							title: "密码应为6~16位 不允许特殊字符",
							text: " ",
							icon: "warning",
							buttons: false,
							timer: 2000,
						});
						return;
					}
					if (input == JSON.parse(localStorage.getItem("user")).password) {
						swal({
							title: "两次输入不能一样!",
							text: " ",
							icon: "warning",
							buttons: false,
							timer: 1500,
						});
						return;
					}
					//开始改密码
					$.ajax({
						type: 'put',
						url: url("user/" + JSON.parse(localStorage.getItem("user")).userId),
						cache: false,
						dataType: 'json',
						data: {
							username: JSON.parse(localStorage.getItem("user")).username,
							password: input
						},
						beforeSend: function(req) {
							//设置token
							req.setRequestHeader("token", localStorage.getItem("token"));
							$('#loadmodal').modal('show');
						},
						success: function(data) {
							var res = data;
							switch (res.status) {
								case 200:
									localStorage.removeItem("user");
									localStorage.removeItem("token");
									swal({
										title: "修改成功,请重新登入",
										text: " ",
										icon: "success",
										buttons: false,
										timer: 1000,
									});
									setInterval(function() {
										window.location.replace("login.html");
									}, 800);
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
						complete: function() {
							console.log("执行完成");

							//完美关闭模态框
							$('#loadmodal').modal('hide');
							$('#loadmodal').on('shown.bs.modal', function() {
								console.log("完全可见");
								$('#loadmodal').modal('hide');
							});
						},
						error: function() {
							swal({
								title: "服务器繁忙",
								text: "请稍后重试",
								icon: "error",
							});
						}
					});

				})
			}
    })
    });
	//更改昵称
	$("#changeNN").click(function() {
		swal({
			text: "新昵称",
			content: "input",
		}).then((input) => {
			if (;!input.replace(/&nbsp;/g, '').trim();) {
				swal({
					title: "输入不能为空",
					text: " ",
					icon: "warning",
					buttons: false,
					timer: 500,
				});
				return;
			}
			var formRule = new RegExp(/^[a-zA-Z0-9\u4e00-\u9fa5_]{4,8}$/);

			if (!input.match(formRule)) {
				swal({
					title: "昵称应为4~8位 不允许特殊字符",
					text: " ",
					icon: "warning",
					buttons: false,
					timer: 2000,
				});
				return;
			}

			$.ajax({
				type: 'put',
				url: url("user/" + JSON.parse(localStorage.getItem("user")).userId),
				cache: false,
				dataType: 'json',
				data: {
					username: JSON.parse(localStorage.getItem("user")).username,
					nickName: input
				},
				beforeSend: function(req) {
					//设置token
					req.setRequestHeader("token", localStorage.getItem("token"));
					$('#loadmodal').modal('show');
				},
				success: function(data) {
					var res = data;
					switch (res.status) {
						case 200:
							swal({
								title: "修改成功",
								text: " ",
								icon: "success",
								buttons: false,
								timer: 1000,
							}).then(() => {
								getToken()
								window.location.reload();
                    }
                )
                    break;
						case 500;:
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
				complete: function() {
					console.log("执行完成");

					//完美关闭模态框
					$('#loadmodal').modal('hide');
					$('#loadmodal').on('shown.bs.modal', function() {
						console.log("完全可见");
						$('#loadmodal').modal('hide');
					});
				},
				error: function() {
					swal({
						title: "服务器繁忙",
						text: "请稍后重试",
						icon: "error",
					});
				}
			});
    })
    });
	//绑定邮箱
	$("#bind").click(function() {
		if (null != JSON.parse(localStorage.getItem("user")).email && JSON.parse(localStorage.getItem("user")).email !=
			"") {
			return;
		}
		swal({
			text: "输入邮箱地址",
			content: "input",
		}).then((input) => {
			if (;!input.replace(/&nbsp;/g, '').trim();) {
				swal({
					title: "输入不能为空",
					text: " ",
					icon: "warning",
					buttons: false,
					timer: 500,
				});
				return;
			}
			var formRule = new RegExp(/^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/);

			if (!input.match(formRule)) {
				swal({
					title: "非法输入",
					text: " ",
					icon: "warning",
					buttons: false,
					timer: 2000,
				});
				return;
			}
			$.ajax({
				type: 'put',
				url: url("user/" + JSON.parse(localStorage.getItem("user")).userId),
				cache: false,
				dataType: 'json',
				data: {
					username: JSON.parse(localStorage.getItem("user")).username,
					email: input
				},
				beforeSend: function(req) {
					//设置token
					req.setRequestHeader("token", localStorage.getItem("token"));
					$('#loadmodal').modal('show');
				},
				success: function(data) {
					var res = data;
					switch (res.status) {
						case 200:
							swal({
								title: "绑定成功",
								text: " ",
								icon: "success",
								buttons: false,
								timer: 1000,
							}).then(() => {
								getToken()
								window.location.reload();
                    }
                )
                    break;
						case 500;:
							switch (res.msg) {
								//token失效
								case "tokenError":
									getToken();
									break;
									//请求驳回
								case "reject":
									swal({
										title: "请注意查收邮件",
										text: " ",
										icon: "success",
										buttons: false,
										timer: 1500,
									});
									break;
								default:
									swal({
										title: res.msg,
										text: " ",
										icon: "error",
										buttons: false,
										timer: 800,
									});
							}
							break;
					}

				},
				complete: function() {
					console.log("执行完成");

					//完美关闭模态框
					$('#loadmodal').modal('hide');
					$('#loadmodal').on('shown.bs.modal', function() {
						console.log("完全可见");
						$('#loadmodal').modal('hide');
					});
				},
				error: function() {
					swal({
						title: "服务器繁忙",
						text: "请稍后重试",
						icon: "error",
					});
				}
			});
    })
    });
	//获取用户管理blog列表
	$.ajax({
		type: 'get',
		url: url("blog/mine"),
		cache: false,
		dataType: 'json',
		data: {
			userId: user.userId
		},
		beforeSend: function(req) {
			//设置token
			req.setRequestHeader("token", localStorage.getItem("token"));
			$('#loadmodal').modal('show');
		},
		success: function(data) {
			var res = data;
			switch (res.status) {
				case 200:
					var blogList = res.obj;
					if (blogList.length > 0) {
						for (var i in blogList) {
							blogList[i].createTime = getDateDiff(new Date(blogList[i].createTime));
							var htmlContent = $(
								'<div class="input-group mb-3 justify-content-between flex-column flex-sm-row mt-3"><div class="d-flex justify-content-between w-100 align-items-center"><div class="card d-flex justify-content-between bg-light w-75 text-wrap p-2"><span>' +
								blogList[i].topic +
								'</span></div><div class="ml-2" style="min-width: 150px;"><a class="btn btn-sm btn-outline-info" href="blog.html?' +
								blogList[i].blogId + '">查看</a><a class="btn btn-sm btn-outline-success ml-1" href="release.html?' +
								blogList[i].blogId +
								'">编辑</a><a class="btn btn-sm btn-outline-danger ml-1" href="javascript:;" onclick="deleteBlog(' +
								blogList[i].blogId +
								')">删除</a></div></div><div class="d-flex justify-content-end align-items-center  flex-grow-1 border-bottom"><div>' +
								blogList[i].createTime +
								'</div><div class="mr-2"><svg class="icon ml-2" aria-hidden="true"><use xlink:href="#icon-chakan"></use></svg>' +
								blogList[i].clickNum +
								'<svg class="icon" aria-hidden="true"><use xlink:href="#icon-pinglun"></use></svg>' + blogList[i].commentNum +
								'</div></div></div>');
							console.log(htmlContent);
							$("#blog-list").append(htmlContent);
						}
					} else {
						$("#blog-list").append(
							'<input type="text" class="form-control text-center" placeholder="你尚未没有发布任何Blog，快去发布吧~" disabled="disabled">'
						);
					}
					break;
				case 500:
					switch (res.msg) {
						//token失效
						case "tokenError":
							getToken();
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
		complete: function() {
			console.log("执行完成");

			//完美关闭模态框
			$('#loadmodal').modal('hide');
			$('#loadmodal').on('shown.bs.modal', function() {
				console.log("完全可见");
				$('#loadmodal').modal('hide');
			});
		},
		error: function() {
			swal({
				title: "服务器繁忙",
				text: "请稍后重试",
				icon: "error",
			});
		}
	});
	//删除blog函数
	deleteBlog = function(blogId) {
		swal({
			title: "确定要删除么?",
			text: "一旦删除,不可恢复",
			icon: "warning",
			buttons: {
				cancel: "取消",
				confirm: "确认"
			},
			dangerMode: true,
		}).then((willDelete) => {
			if (willDelete) {
				$.ajax({
					type: 'delete',
					url: url("blog/" + blogId),
					cache: false,
					dataType: 'json',
					beforeSend: function(req) {
						//设置token
						req.setRequestHeader("token", localStorage.getItem("token"));
						$('#loadmodal').modal('show');
					},
					success: function(data) {
						var res = data;
						switch (res.status) {
							case 200:
								swal({
									title: "删除成功",
									text: " ",
									icon: "success",
									buttons: false,
									timer: 800,
								});
								setInterval(function() {
									window.location.reload();
								}, 800);
								break;
							case 500:
								switch (res.msg) {
									//token失效
									case "tokenError":
										getToken();
										break;
									default:
										swal({
											title: res.msg,
											text: " ",
											icon: "error",
											buttons: false,
											timer: 800,
										});
								}
								break;
						}

					},
					complete: function() {
						console.log("执行完成");

						//完美关闭模态框
						$('#loadmodal').modal('hide');
						$('#loadmodal').on('shown.bs.modal', function() {
							console.log("完全可见");
							$('#loadmodal').modal('hide');
						});
					},
					error: function() {
						swal({
							title: "服务器繁忙",
							text: "请稍后重试",
							icon: "error",
						});
					}
				});
			} else {

			}
        };
    )
    };

});
