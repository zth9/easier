$(function () {
	//用来标注当前菜单项
	$("#nav-list li:eq(0) a").addClass("nav-active");

	//用来显示手机版 图标
	$("#title span").text("首页");
	$("#title svg use").attr("xlink:href", "#icon-shouye");

	//登录显示隐藏
	if (null != localStorage.getItem("user")) {
		$("#goRelease").removeClass("d-none");
	}
	//加载Blog
	loadBlog = function () {
		var rule = 1;
		var userRule = localStorage.getItem("rule");
		if (userRule != null) {
			rule = userRule;
		}
		if (rule == 1) {
			$("#timeBtn").addClass("text-success");
		} else if (rule == 2){
			$("#hotBtn").addClass("text-success");
		}
		$.ajax({
			type: 'get',
			url: url("blog"),
			cache: false,
			dataType: 'json',
			data: {
				rule: rule,
			},
			beforeSend: function () {
				//动画
				$('#loadmodal').modal('show')
			},
			success: function (data) {
				var res = data;
				switch (res.status) {
					//发布成功
					case 200:
						//渲染Blog列表
						var blogList = res.obj;
						console.log(blogList);
						for (var blog in blogList) {
							var javaDate = blogList[blog].createTime;
							//blogList[blog].createTime = fmtDate(new Date(javaDate), "datetime")
							blogList[blog].createTime = getDateDiff(new Date(javaDate));
							var li = $('<li class="list-group-item"></li>');
							var mainDiv = $('<div class="d-flex align-items-center"></div>');
							mainDiv.append('<div><a href="user/index/' + blogList[blog].userId + '" ><img src ="' + blogList[blog].headPic + '" width = "45px" height = "45px" /></a ></div>');
							var div1 = $('<div class="flex-grow-1"></div>');
							//根据不同类型文章,设定不同显示颜色标题
							if (blogList[blog].tagId == 1) {
								//置顶文章
								div1.append('<div><a class="text-danger" href="blog.html?' + blogList[blog].blogId + '">' + blogList[blog].topic + '</a></div>')
							} else {
								div1.append('<div><a class="text-dark" href="blog.html?' + blogList[blog].blogId + '">' + blogList[blog].topic + '</a></div>')
							}


							div1.append('<div class="d-flex justify-content-between  flex-column flex-sm-row"><div class="d-flex align-items-center"><a class="text-info mr-2 text-decoration-none" href="user/index/' + blogList[blog].userId + '">' + blogList[blog].nickName + '</a><svg class="icon" aria-hidden="true"><use xlink:href="#icon-dingshi"></use></svg><span class=" bg-light rounded">' + blogList[blog].createTime + '</span></div><div class="d-flex  justify-content-end"><div class="d-flex align-items-center  mr-2"><svg class="icon" aria-hidden="true"><use xlink:href="#icon-chakan"></use></svg>' + blogList[blog].clickNum + '</div><div class="d-flex align-items-center"><svg class="icon" aria-hidden="true"><use xlink:href="#icon-pinglun"></use></svg>' + blogList[blog].commentNum + '</div></div></div>');
							mainDiv.append(div1);
							li.append(mainDiv);

							$("#blog-list").append(li);
						}

						//加载置顶文章
						$.ajax({
							type: 'get',
							url: url("blog/topping"),
							cache: false,
							dataType: 'json',
							data: {

							},
							success: function (data) {
								var res = data;
								switch (res.status) {
									//加载公告成功
									case 200:
										var toppingList = res.obj;
										for (var i in toppingList) {
											$("#topping").append('<li class="list-group-item border-left-0 border-right-0 d-flex"><div class="col-8 p-0"><a class="text-decoration-none" href="blog.html?' + toppingList[i].blogId + '">' + toppingList[i].topic + '</a></div><div class="col-4 p-0 d-flex justify-content-end"><div class="d-flex"><div class="d-flex justify-content-center align-content-center"><div><svg class="icon" aria-hidden="true"><use xlink:href="#icon-chakan"></use></svg></div></div><div class="d-flex justify-content-center align-content-center">' + toppingList[i].clickNum + '</div></div><div class="d-flex"><div class="d-flex justify-content-center align-content-center"><div><svg class="icon" aria-hidden="true"><use xlink:href="#icon-pinglun "></use></svg></div></div><div class="d-flex justify-content-center align-content-center">' + toppingList[i].commentNum + '</div></div></div></li>')
										}
										break;
									case 500:
										swal("失败", res.msg, "error");
										break;
								}

							},
							error: function () {
								swal({
									title: "服务器繁忙",
									text: "请稍后重试",
									icon: "error",
								});
							}
						});
						break;
					case 500:
						swal("失败", res.msg, "error");
						break;
				}
			},
			complete: function () {
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
	};
	$("#search").click(function () {
		var keyWordInput = $("#searchContent");
		if ("" != keyWordInput.val()) {
			var keyWord = keyWordInput.val();
			var searchRule = new RegExp(/^[a-zA-Z0-9\u4e00-\u9fa5_]{1,10}$/);
			if (keyWord.match(searchRule)) {
				window.location.replace("/easier/index.html?search=" + keyWord);
				return;
			}
			keyWordInput.val("");
			swal({
				title: "非法的搜索参数",
				text: " ",
				icon: "warning",
				buttons: false,
				timer: 700,
			});
		}
	});
	loadSearch = function (keyWord) {
		$.ajax({
			type: 'get',
			url: url("blog/search/" + keyWord),
			cache: false,
			dataType: 'json',
			data: {
				rule: 1,
			},
			beforeSend: function () {
				//动画

				$('#loadmodal').modal('show')
			},
			success: function (data) {
				var res = data;
				switch (res.status) {
					//发布成功
					case 200:
						//渲染Blog列表
						var blogList = res.obj;
						console.log(blogList);
						for (var blog in blogList) {
							var javaDate = blogList[blog].createTime;
							//blogList[blog].createTime = fmtDate(new Date(javaDate), "datetime")
							blogList[blog].createTime = getDateDiff(new Date(javaDate));
							var li = $('<li class="list-group-item"></li>');
							var mainDiv = $('<div class="d-flex align-items-center"></div>');
							mainDiv.append('<div><a href="user/index/' + blogList[blog].userId + '" ><img src ="' + blogList[blog].headPic + '" width = "45px" height = "45px" /></a ></div>');
							var div1 = $('<div class="flex-grow-1"></div>');
							//根据不同类型文章,设定不同显示颜色标题
							if (blogList[blog].tagId == 1) {
								//置顶文章
								div1.append('<div><a class="text-danger" href="blog.html?' + blogList[blog].blogId + '">' + blogList[blog].topic + '</a></div>')
							} else {
								div1.append('<div><a class="text-dark" href="blog.html?' + blogList[blog].blogId + '">' + blogList[blog].topic + '</a></div>')
							}


							div1.append('<div class="d-flex justify-content-between  flex-column flex-sm-row"><div class="d-flex align-items-center"><a class="text-info mr-2 text-decoration-none" href="user/index/' + blogList[blog].userId + '">' + blogList[blog].nickName + '</a><svg class="icon" aria-hidden="true"><use xlink:href="#icon-dingshi"></use></svg><span class=" bg-light rounded">' + blogList[blog].createTime + '</span></div><div class="d-flex  justify-content-end"><div class="d-flex align-items-center  mr-2"><svg class="icon" aria-hidden="true"><use xlink:href="#icon-chakan"></use></svg>' + blogList[blog].clickNum + '</div><div class="d-flex align-items-center"><svg class="icon" aria-hidden="true"><use xlink:href="#icon-pinglun"></use></svg>' + blogList[blog].commentNum + '</div></div></div>');
							mainDiv.append(div1);
							li.append(mainDiv);

							$("#blog-list").append(li);
						}

						//加载置顶文章
						$.ajax({
							type: 'get',
							url: url("blog/topping"),
							cache: false,
							dataType: 'json',
							data: {

							},
							success: function (data) {
								var res = data;
								switch (res.status) {
									//加载公告成功
									case 200:
										var toppingList = res.obj;
										for (var i in toppingList) {
											$("#topping").append('<li class="list-group-item border-left-0 border-right-0 d-flex"><div class="col-8 p-0"><a class="text-decoration-none" href="blog.html?' + toppingList[i].blogId + '">' + toppingList[i].topic + '</a></div><div class="col-4 p-0 d-flex justify-content-end"><div class="d-flex"><div class="d-flex justify-content-center align-content-center"><div><svg class="icon" aria-hidden="true"><use xlink:href="#icon-chakan"></use></svg></div></div><div class="d-flex justify-content-center align-content-center">' + toppingList[i].clickNum + '</div></div><div class="d-flex"><div class="d-flex justify-content-center align-content-center"><div><svg class="icon" aria-hidden="true"><use xlink:href="#icon-pinglun "></use></svg></div></div><div class="d-flex justify-content-center align-content-center">' + toppingList[i].commentNum + '</div></div></div></li>')
										}
										break;
									case 500:
										swal("失败", res.msg, "error");
										break;
								}

							},
							error: function () {
								swal({
									title: "服务器繁忙",
									text: "请稍后重试",
									icon: "error",
								});
							}
						});
						break;
					case 500:
						swal("失败", res.msg, "error");
						break;
				}
			},
			complete: function () {
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
	};
	//有搜索参数 执行搜索函数 否则执行全局函数
	var urlStr = window.location.search;
	urlStr = decodeURIComponent(urlStr);
	if ("" != urlStr) {
		console.log("urlStr:" + urlStr);
		var keyWord = urlStr.substr(8);
		var searchRule = new RegExp(/^[a-zA-Z0-9\u4e00-\u9fa5_]{1,10}$/);
		if (keyWord.match(searchRule)) {
			loadSearch(keyWord);
			return;
		}
		swal({
			title: "非法的搜索参数",
			text: " ",
			icon: "warning",
			buttons: false,
			timer: 700,
		});
		window.location.replace("/easier/index.html");
	} else {
		loadBlog();
	}

	//按时间加载blog
	loadBlogByTime = function () {
		localStorage.setItem("rule", 1);
		window.location.reload();
	};
	//按热度加载blog
	loadBlogByHot = function () {
		localStorage.setItem("rule", 2);
		window.location.reload();
	}
});