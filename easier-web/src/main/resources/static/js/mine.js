
$(function () {
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
		case "todo":
			$("#todo-manage").addClass('active');
			$("#todo").addClass('show');
			$("#todo").addClass('active');
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
	if (user.email != null && user.email!="") {
		$("#user-email").attr("placeholder", user.email);
	} else {
		$("#user-email").attr("placeholder", "未绑定");
		$("#bind-email").append('<a id="bind" class="btn btn-primary" href="#">绑定邮箱</a>');
	}
	//选择头像
	$("#changePic").click(function () {
		$("#choiceFile").click();
	});
	//更改显示
	$("#choiceFile").change(function(){
		$("#user-headPic").attr("src",URL.createObjectURL($(this)[0].files[0]));
		$("#upload").removeClass("disabled");
		$("#upload").removeClass("d-none");
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
	$("#upload").click(function () {
		var formData = new FormData();
		formData.append("file", $("#choiceFile")[0].files[0]);
		$.ajax({
			type: 'PUT',
			url: url("user/uploadAvatar"),
			data: formData,
			beforeSend: function (req) {
				//设置token
				req.setRequestHeader("token", localStorage.getItem("token"));
				$('#loadmodal').modal('show');
			},
			processData: false,
			contentType: false,
			success: function (res) {
				switch (res.status) {
					case 200:
						swal({
							title: "修改成功",
							text: " ",
							icon: "success",
							buttons: false,
							timer: 800,
						});
						var localUser = JSON.parse(localStorage.getItem("user"));
						localUser.headPic = res.obj.newHeadPic;
						localStorage.setItem("user", JSON.stringify(localUser));
						setInterval(function () {
							window.location.reload();
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
	});
	//更改密码
	$("#changePwd").click(function () {
		swal({
			text: "请输入旧密码",
			content: "input",
		}).then((input) => {
			if (input != JSON.parse(localStorage.getItem("user")).password) {
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
				if (!input.replace(/&nbsp;/g, '').trim()) {
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
				beforeSend: function (req) {
					//设置token
					req.setRequestHeader("token", localStorage.getItem("token"));
					$('#loadmodal').modal('show');
				},
				success: function (data) {
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
							setInterval(function () {
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
					})
				}
			})

		})
		}
	})
	});
	//更改昵称
	$("#changeNN").click(function () {
		swal({
			text: "新昵称",
			content: "input",
		}).then((input) => {
			if (!input.replace(/&nbsp;/g, '').trim()) {
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
			beforeSend: function (req) {
				//设置token
				req.setRequestHeader("token", localStorage.getItem("token"));
				$('#loadmodal').modal('show');
			},
			success: function (data) {
				var res = data;
				switch (res.status) {
					case 200:
						swal({
							title: "修改成功",
							text: " ",
							icon: "success",
							buttons: false,
							timer: 1000,
						});
						//更改localStroage
						setInterval(function () {
							localStorage.setItem("user", JSON.stringify(res.obj));
							window.location.reload();
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
			complete: function () {

				
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
	})
	});
	//绑定邮箱
	$("#bind").click(function () {
		if (null != JSON.parse(localStorage.getItem("user")).email && JSON.parse(localStorage.getItem("user")).email != "") {
			return;
		}
		swal({
			text: "输入邮箱地址",
			content: "input",
		}).then((input) => {
			if (!input.replace(/&nbsp;/g, '').trim()) {
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
			beforeSend: function (req) {
				//设置token
				req.setRequestHeader("token", localStorage.getItem("token"));
				$('#loadmodal').modal('show');
			},
			success: function (data) {
				var res = data;
				switch (res.status) {
					case 200:
						swal({
							title: "绑定成功",
							text: " ",
							icon: "success",
							buttons: false,
							timer: 1000,
						});
						setInterval(function () {
							localStorage.setItem("user", JSON.stringify(res.obj));
							window.location.reload();
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
			complete: function () {

				
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
		beforeSend: function (req) {
			//设置token
			req.setRequestHeader("token", localStorage.getItem("token"));
			$('#loadmodal').modal('show');
		},
		success: function (data) {
			var res = data;
			switch (res.status) {
				case 200:
					var blogList = res.obj;
					if (blogList.length > 0) {
						for (var i in blogList) {
							blogList[i].createTime = getDateDiff(new Date(blogList[i].createTime));
							var htmlContent = $('<div class="input-group mb-3 justify-content-between flex-column flex-sm-row mt-3"><div class="d-flex justify-content-between w-100 align-items-center"><div class="card d-flex justify-content-between bg-light w-75 text-wrap p-2"><span>' + blogList[i].topic + '</span></div><div class="ml-2" style="min-width: 150px;"><a class="btn btn-sm btn-outline-info" href="blog.html?' + blogList[i].blogId + '">查看</a><a class="btn btn-sm btn-outline-success ml-1" href="release.html?' + blogList[i].blogId + '">编辑</a><a class="btn btn-sm btn-outline-danger ml-1" href="javascript:;" onclick="deleteBlog(' + blogList[i].blogId + ')">删除</a></div></div><div class="d-flex justify-content-end align-items-center  flex-grow-1 border-bottom"><div>' + blogList[i].createTime + '</div><div class="mr-2"><svg class="icon ml-2" aria-hidden="true"><use xlink:href="#icon-chakan"></use></svg>' + blogList[i].clickNum + '<svg class="icon" aria-hidden="true"><use xlink:href="#icon-pinglun"></use></svg>' + blogList[i].commentNum +'</div></div></div>');
							$("#blog-list").append(htmlContent);
						}
					} else {
						$("#blog-list").append('<input type="text" class="form-control text-center" placeholder="你尚未没有发布任何Blog，快去发布吧~" disabled="disabled">');
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
		complete: function () {
			
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
	//删除blog函数
	deleteBlog = function (blogId) {
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
					beforeSend: function (req) {
						//设置token
						req.setRequestHeader("token", localStorage.getItem("token"));
						$('#loadmodal').modal('show');
					},
					success: function (data) {
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
								setInterval(function () {
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

			}
		}
	)
	};

	//获取用户待办列表
	$.ajax({
		type: 'get',
		url: url("todo"),
		cache: false,
		dataType: 'json',
		beforeSend: function (req) {
			//设置token
			req.setRequestHeader("token", localStorage.getItem("token"));
		},
		success: function (data) {
			var res = data;
			switch (res.status) {
				case 200:
					var todoList = res.obj;
					if (todoList.length > 0) {
						for (var i in todoList) {
							var curTodo = todoList[i];
							var curTodoStatus = curTodo.todoStatus;
							var curTodoType = curTodo.todoType;
							// curTodo.todoTime = getDateDiff()
							if (curTodoStatus==0){
								//正在路上
								if (curTodoType==0){
									$("#todo-going-c").append('<div class="going-item d-flex w-100 justify-content-between border-bottom"><div class="w-75 d-flex justify-content-between"><div class="input-group mb-3 justify-content-between flex-column flex-sm-row mt-3"><div class="d-flex justify-content-between w-100 align-items-center"><div class="card d-flex justify-content-between bg-light w-100 text-wrap p-2 mr-1"><span id="todoContent'+curTodo.todoId+'">'+curTodo.todoContent+'</span></div></div></div><div class="d-flex align-items-center"><div class="mr-1 d-flex justify-content-center align-content-center"><span class="todo-time bg-light d-flex justify-content-center align-items-center rounded text-nowrap" id="todoTime'+curTodo.todoId+'">'+curTodo.todoTime+'</span></div><svg class="icon d-flex justify-content-center align-content-center" style="font-size:13px" aria-hidden="true"><use xlink:href="#icon-qiandao"></use></svg></div></div><div class="w-25 d-flex justify-content-center"><div class="mr-1 d-flex align-items-center"><a class="btn btn-sm btn-outline-success text-success text-nowrap"  onclick="editTodo('+curTodo.todoId+')">编辑</a></div><div class="d-flex align-items-center"><a class="btn btn-sm btn-outline-danger text-danger text-nowrap"  onclick="deleteTodo('+curTodo.todoId+')">删除</a></div></div></div>');
								}else {
									$("#todo-going-c").append('<div class="going-item d-flex w-100 justify-content-between border-bottom"><div class="w-75 d-flex justify-content-between"><div class="input-group mb-3 justify-content-between flex-column flex-sm-row mt-3"><div class="d-flex justify-content-between w-100 align-items-center"><div class="card d-flex justify-content-between bg-light w-100 text-wrap p-2 mr-1"><span id="todoContent'+curTodo.todoId+'">'+curTodo.todoContent+'</span></div></div></div><div class="d-flex align-items-center"><div class="mr-1 d-flex justify-content-center align-content-center"><span class="todo-time bg-light d-flex justify-content-center align-items-center rounded text-nowrap" id="todoTime'+curTodo.todoId+'">'+curTodo.todoTime+'</span></div><svg class="icon d-flex justify-content-center align-content-center" style="font-size:13px" aria-hidden="true"><use xlink:href="#icon-zhouqi"></use></svg></div></div><div class="w-25 d-flex justify-content-center"><div class="mr-1 d-flex align-items-center"><a class="btn btn-sm btn-outline-success text-success text-nowrap"  onclick="editTodo('+curTodo.todoId+')">编辑</a></div><div class="d-flex align-items-center"><a class="btn btn-sm btn-outline-danger text-danger text-nowrap"  onclick="deleteTodo('+curTodo.todoId+')">删除</a></div></div></div>');
								}
							}else {
								//阵亡
								if (curTodoType==0){
									$("#todo-death-c").append('<div class="going-item d-flex w-100 justify-content-between border-bottom"><div class="w-75 d-flex justify-content-between"><div class="input-group mb-3 justify-content-between flex-column flex-sm-row mt-3"><div class="d-flex justify-content-between w-100 align-items-center"><div class="card d-flex justify-content-between bg-light w-100 text-wrap p-2 mr-1"><span id="todoContent'+curTodo.todoId+'">'+curTodo.todoContent+'</span></div></div></div><div class="d-flex align-items-center"><div class="mr-1 d-flex justify-content-center align-content-center"><span class="todo-time bg-light d-flex justify-content-center align-items-center rounded text-nowrap" id="todoTime'+curTodo.todoId+'">'+curTodo.todoTime+'</span></div><svg class="icon d-flex justify-content-center align-content-center" style="font-size:13px" aria-hidden="true"><use xlink:href="#icon-qiandao"></use></svg></div></div><div class="w-25 d-flex justify-content-center"><div class="d-flex align-items-center"><a class="btn btn-sm btn-outline-danger text-danger text-nowrap"  onclick="deleteTodo('+curTodo.todoId+')">删除</a></div></div></div>');
								}else {
									$("#todo-death-c").append('<div class="going-item d-flex w-100 justify-content-between border-bottom"><div class="w-75 d-flex justify-content-between"><div class="input-group mb-3 justify-content-between flex-column flex-sm-row mt-3"><div class="d-flex justify-content-between w-100 align-items-center"><div class="card d-flex justify-content-between bg-light w-100 text-wrap p-2 mr-1"><span id="todoContent'+curTodo.todoId+'">'+curTodo.todoContent+'</span></div></div></div><div class="d-flex align-items-center"><div class="mr-1 d-flex justify-content-center align-content-center"><span class="todo-time bg-light d-flex justify-content-center align-items-center rounded text-nowrap" id="todoTime'+curTodo.todoId+'">'+curTodo.todoTime+'</span></div><svg class="icon d-flex justify-content-center align-content-center" style="font-size:13px" aria-hidden="true"><use xlink:href="#icon-zhouqi"></use></svg></div></div><div class="w-25 d-flex justify-content-center"><div class="d-flex align-items-center"><a class="btn btn-sm btn-outline-danger text-danger text-nowrap"  onclick="deleteTodo('+curTodo.todoId+')">删除</a></div></div></div>');
								}
							}
						}
					}
					break;
				case 500:
					switch (res.msg) {
						//token失效
						case "tokenError":
							getToken();
							break;
						default:
							console.log("服务器繁忙")
					}
					break;
			}
		},
		error: function () {
			console.log("服务器繁忙")
		}
	});
	//编辑待办
	editTodo = function(todoId){
		$("#add-Modal").modal('show');
		alert("$('#todoContent'+todoId).val()"+$('#todoContent'+todoId).text());
		$("#todo-content").val($('#todoContent'+todoId).text());
		alert("$('#todoTime'+todoId).val()"+$('#todoTime'+todoId).text());
		$("#picker").val($('#todoTime'+todoId).text());
		globalTodoId = todoId;
	};

	//删除待办
	deleteTodo = function (todoId) {
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
						url: url("todo/"+todoId),
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
									swal({
										title: "删除成功",
										text: " ",
										icon: "success",
										buttons: false,
										timer: 800,
									});
									setInterval(function () {
										window.location.reload();
									}, 800);
									break;
								case 500:
									switch (res.msg) {
										//token失效
										case "tokenError":
											getToken();
											break;
										case "reject":
											swal({
												title: "非法操作",
												text: " ",
												icon: "success",
												buttons: false,
												timer: 800,
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
						complete: function () {
							
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
				}
			}
		)
	};
	//提交待办
	$("#submit-todo").click(function () {
		var todoContent = $("#todo-content").val();
		if (!todoContent.replace(/&nbsp;/g, '').trim()) {
			swal({
				title: "输入不能为空",
				text: " ",
				icon: "warning",
				buttons: false,
				timer: 500,
			});
			return;
		}
		var todoTime = $("#picker").val();
		var curDate = new Date();
		var targetTodoTime = new Date(todoTime);
		if (!todoTime.replace(/&nbsp;/g, '').trim() || targetTodoTime.getTime()<curDate.getTime()){
			swal({
				title: "提醒时间不合法",
				text: " ",
				icon: "warning",
				buttons: false,
				timer: 500,
			});
			return;
		}

		var safeTodoContent = filterXSS(todoContent);
		var todoType = $("#repeat-radio input[name='repeat']:checked").val();

		//ajax新增提醒
		$.ajax({
			type: 'post',
			url: url("todo"),
			cache: false,
			dataType: 'json',
			data: {
				todoId: globalTodoId,
				todoTimeStr: todoTime,
				todoContent: safeTodoContent,
				todoType: todoType
			},
			beforeSend: function (req) {
				//设置token
				req.setRequestHeader("token", localStorage.getItem("token"));
				//清空globalTodoId
				globalTodoId = "";
				$('#add-Modal').modal('hide');
				$('#loadmodal').modal('show');
			},
			success: function (data) {
				var res = data;
				switch (res.status) {
					case 200:
						swal({
							title: res.msg,
							text: " ",
							icon: "success",
							buttons: false,
							timer: 1000,
						});
						//更改localStroage
						setInterval(function () {
							window.location.replace("mine.html?todo");
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
									buttons: true,
								});
						}
						break;
				}
			},
			complete: function () {
				
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