(function(w) {
	var app = {
		"changeType" : function(select) {
			var value = $(select).val();
			console.log(value);
			if (value == 20) {// shell
				$("div[httpCallout]").show();
				$("div[agent]").hide();
				$("div[command]").hide();
			} else {
				$("div[httpCallout]").hide();
				$("div[agent]").show();
				$("div[command]").show();
			}
		},
		"openTriggerTaskModal" : function(triggerTaskId) {
			$('#triggerTaskModal input[taskId]').val(triggerTaskId);
			$('#triggerTaskModal').modal('show');
		},
		"triggerTask" : function(form) {
			var triggerTaskId = $('#triggerTaskModal input[taskId]').val();
			$("#triggerTaskModal div[alertMessage]").text('');

			// 显示执行中
			var t = $("#task_" + triggerTaskId).find("li[trigger]");
			if (t) {
				t.attr("switch_off", "");
				t.removeAttr("switch_on");
			}

			$.ajax({
				type : $(form).attr('method'),
				url : $(form).attr('action'),
				data : $(form).serialize(),
				dataType : "json",
				success : app.triggerTaskDone(triggerTaskId),
				error : app.httpError
			});
			return false;
		},
		"triggerTaskDone" : function(triggerTaskId) {
			return function(data) {
				if (data.success == false) {
					$("#triggerTaskModal div[alertMessage]").html(
							$("#alert_error").html());
					$("#triggerTaskModal div[alertMessage] > div > span").text(
							data.errorMsg);

					// 恢复执行按钮
					var t = $("#task_" + triggerTaskId).find("li[trigger]");
					if (t) {
						t.attr("switch_on", "");
						t.removeAttr("switch_off");
					}

				} else {
					w.location = w.contextPath + "/task/instance?taskId="
							+ triggerTaskId;
				}
			}
		},
		"openTriggerUpgradeModal" : function(triggerTaskId) {
			if (!triggerTaskId) {
				app.appError("错误", "该agent没有对应的升级任务！");
			} else {
				$('#triggerUpgradeModal input[taskId]').val(triggerTaskId);
				$('#triggerUpgradeModal').modal('show');
			}
		},
		"triggerUpgrade" : function(form) {
			var triggerTaskId = $('#triggerUpgradeModal input[taskId]').val();
			$("#triggerUpgradeModal div[alertMessage]").text('');
			$.ajax({
				type : $(form).attr('method'),
				url : $(form).attr('action'),
				data : $(form).serialize(),
				dataType : "json",
				success : app.triggerUpgradeDone(triggerTaskId),
				error : app.httpError
			});
			return false;
		},
		"triggerUpgradeDone" : function(triggerTaskId) {
			return function(data) {
				if (data.success == false) {
					$("#triggerUpgradeModal div[alertMessage]").html(
							$("#alert_error").html());
					$("#triggerUpgradeModal div[alertMessage] > div > span")
							.text(data.errorMsg);
				} else {
					$('#triggerUpgradeModal').modal('hide');
					var url = w.contextPath + "/task/instance?taskId="
							+ triggerTaskId;
					w.open(url);
				}
			}
		},
		"openTriggerWorkflowInstanceCancelModal" : function(workflowInstanceId) {
			$(
					'#openTriggerWorkflowInstanceCancelModal input[workflowInstanceId]')
					.val(workflowInstanceId);
			$('#openTriggerWorkflowInstanceCancelModal').modal('show');
		},
		"cancelWorkflowInstance" : function(form) {
			var workflowInstanceId = $(
					'#openTriggerWorkflowInstanceCancelModal input[workflowInstanceId]')
					.val();
			$("#openTriggerWorkflowInstanceCancelModal div[alertMessage]")
					.text('');
			$.ajax({
				type : $(form).attr('method'),
				url : $(form).attr('action'),
				data : $(form).serialize(),
				dataType : "json",
				success : app.cancelWorkflowInstanceDone(workflowInstanceId),
				error : app.httpError
			});
			return false;
		},
		"cancelWorkflowInstanceDone" : function(workflowInstanceId) {
			return function(data) {
				if (data.success == false) {
					$(
							"#openTriggerWorkflowInstanceCancelModal div[alertMessage]")
							.html($("#alert_error").html());
					$(
							"#openTriggerWorkflowInstanceCancelModal div[alertMessage] > div > span")
							.text(data.errorMsg);
				} else {
					w.location.reload();
				}
			}
		},
		"openCancelTaskModal" : function(instanceId) {
			$('#cancelTaskModal input[instanceId]').val(instanceId);
			$('#cancelTaskModal').modal('show');
		},
		"cancelTask" : function(form) {
			var instanceId = $('#cancelTaskModal input[instanceId]').val();
			$("#cancelTaskModal div[alertMessage]").text('');
			$.ajax({
				type : $(form).attr('method'),
				url : $(form).attr('action'),
				data : $(form).serialize(),
				dataType : "json",
				success : app.cancelTaskDone(instanceId),
				error : app.httpError
			});
			return false;
		},
		"cancelTaskDone" : function(taskId) {
			return function(data) {
				if (data.success == false) {
					$("#cancelTaskModal div[alertMessage]").html(
							$("#alert_error").html());
					$("#cancelTaskModal div[alertMessage] > div > span").text(
							data.errorMsg);
				} else {
					$('#cancelTaskModal').modal('hide');
					w.location.reload();
				}
			}
		},
		"openRerunTaskModal" : function(instanceId) {
			$('#rerunTaskModal input[instanceId]').val(instanceId);
			$('#rerunTaskModal').modal('show');
		},
		"rerunTask" : function(form) {
			var instanceId = $('#rerunTaskModal input[instanceId]').val();
			$("#rerunTaskModal div[alertMessage]").text('');
			$.ajax({
				type : $(form).attr('method'),
				url : $(form).attr('action'),
				data : $(form).serialize(),
				dataType : "json",
				success : app.rerunTaskDone(instanceId),
				error : app.httpError
			});
			return false;
		},
		"rerunTaskDone" : function(taskId) {
			return function(data) {
				if (data.success == false) {
					$("#rerunTaskModal div[alertMessage]").html(
							$("#alert_error").html());
					$("#rerunTaskModal div[alertMessage] > div > span").text(
							data.errorMsg);
				} else {
					$('#rerunTaskModal').modal('hide');
					w.location.reload();
				}
			}
		},
		"openRerunWorkflowModal" : function(instanceId) {
			$('#rerunWorkflowModal input[instanceId]').val(instanceId);
			$('#rerunWorkflowModal').modal('show');
		},
		"rerunWorkflow" : function(form) {
			var instanceId = $('#rerunWorkflowModal input[instanceId]').val();
			$("#rerunWorkflowModal div[alertMessage]").text('');
			$.ajax({
				type : $(form).attr('method'),
				url : $(form).attr('action'),
				data : $(form).serialize(),
				dataType : "json",
				success : app.rerunWorkflowDone(instanceId),
				error : app.httpError
			});
			return false;
		},
		"rerunWorkflowDone" : function(taskId) {
			return function(data) {
				if (data.success == false) {
					$("#rerunWorkflowModal div[alertMessage]").html(
							$("#alert_error").html());
					$("#rerunWorkflowModal div[alertMessage] > div > span")
							.text(data.errorMsg);
				} else {
					$('#rerunWorkflowModal').modal('hide');
					w.location.reload();
				}
			}
		},
		"openCreateUserModal" : function() {
			$('#createUserModal').modal('show');
			$("#createUserModal input[name='username']").focus();
		},
		"createUser" : function(form) {
			$("#createUserModal div[alertMessage]").text('');
			$.ajax({
				type : $(form).attr('method'),
				url : $(form).attr('action'),
				data : $(form).serialize(),
				dataType : "json",
				success : app.createUserDone,
				error : app.httpError
			});
			return false;
		},
		"createUserDone" : function(data) {
			if (data.success == false) {
				$("#createUserModal div[alertMessage]").html(
						$("#alert_error").html());
				$("#createUserModal div[alertMessage] > div > span").text(
						data.errorMsg);
			} else {
				w.location = w.contextPath + "/user";
			}
		},
		"openCreateTeamModal" : function() {
			$('#createTeamModal').modal('show');
			$("#createTeamModal input[name='teamname']").focus();
		},
		"createTeam" : function(form) {
			$("#createTeamModal div[alertMessage]").text('');
			$.ajax({
				type : $(form).attr('method'),
				url : $(form).attr('action'),
				data : $(form).serialize(),
				dataType : "json",
				success : app.createTeamDone,
				error : app.httpError
			});
			return false;
		},
		"createTeamDone" : function(data) {
			if (data.success == false) {
				$("#createTeamModal div[alertMessage]").html(
						$("#alert_error").html());
				$("#createTeamModal div[alertMessage] > div > span").text(
						data.errorMsg);
			} else {
				w.location = w.contextPath + "/team";
			}
		},
		"openRemoveUserModal" : function(removeUserId, removeUserName) {
			$('#removeUserModal input[userId]').val(removeUserId);
			$('#removeUserModal span[username]').text(removeUserName);
			$('#removeUserModal').modal('show');
		},
		"removeUser" : function(form) {
			var removeUserId = $('#removeUserModal input[userId]').val();
			$("#removeUserModal div[alertMessage]").text('');
			$.ajax({
				type : $(form).attr('method'),
				url : $(form).attr('action'),
				data : $(form).serialize(),
				dataType : "json",
				success : app.removeUserDone(removeUserId),
				error : app.httpError
			});
			return false;
		},
		"removeUserDone" : function(removeUserId) {
			return function(data) {
				if (data.success == false) {
					$("#removeUserModal div[alertMessage]").html(
							$("#alert_error").html());
					$("#removeUserModal div[alertMessage] > div > span").text(
							data.errorMsg);
				} else {
					w.location = w.contextPath + "/user";
				}
			}
		},
		"openRemoveTeamModal" : function(id, name) {
			$('#removeTeamModal input[teamId]').val(id);
			$('#removeTeamModal span[teamName]').text(name);
			$('#removeTeamModal').modal('show');
		},
		"removeTeam" : function(form) {
			var id = $('#removeTeamModal input[teamId]').val();
			$("#removeTeamModal div[alertMessage]").text('');
			$.ajax({
				type : $(form).attr('method'),
				url : $(form).attr('action'),
				data : $(form).serialize(),
				dataType : "json",
				success : app.removeTeamDone(id),
				error : app.httpError
			});
			return false;
		},
		"removeTeamDone" : function(id) {
			return function(data) {
				if (data.success == false) {
					$("#removeTeamModal div[alertMessage]").html(
							$("#alert_error").html());
					$("#removeTeamModal div[alertMessage] > div > span").text(
							data.errorMsg);
				} else {
					w.location = w.contextPath + "/team";
				}
			}
		},
		"openRenameTeamModal" : function(id, name) {
			$('#renameTeamModal input[teamId]').val(id);
			$('#renameTeamModal input[teamName]').val(name);
			$('#renameTeamModal').modal('show');
		},
		"renameTeam" : function(form) {
			var id = $('#renameTeamModal input[teamId]').val();
			var name = $('#renameTeamModal input[teamName]').val();
			$("#renameTeamModal div[alertMessage]").text('');
			$.ajax({
				type : $(form).attr('method'),
				url : $(form).attr('action'),
				data : $(form).serialize(),
				dataType : "json",
				success : app.renameTeamDone(id),
				error : app.httpError
			});
			return false;
		},
		"renameTeamDone" : function(id) {
			return function(data) {
				if (data.success == false) {
					$("#renameTeamModal div[alertMessage]").html(
							$("#alert_error").html());
					$("#renameTeamModal div[alertMessage] > div > span").text(
							data.errorMsg);
				} else {
					w.location = w.contextPath + "/team";
				}
			}
		},
		"openResetPasswordModal" : function(userId, username) {
			$('#resetPasswordModal input[userId]').val(userId);
			$('#resetPasswordModal span[username]').text(username);
			$('#resetPasswordModal').modal('show');
		},
		"resetPassword" : function(form) {
			var userId = $('#resetPasswordModal input[userId]').val();
			$("#resetPasswordModal div[alertMessage]").text('');
			$.ajax({
				type : $(form).attr('method'),
				url : $(form).attr('action'),
				data : $(form).serialize(),
				dataType : "json",
				success : app.resetPasswordDone(userId),
				error : app.httpError
			});
			return false;
		},
		"resetPasswordDone" : function(userId) {
			return function(data) {
				if (data.success == false) {
					$("#resetPasswordModal div[alertMessage]").html(
							$("#alert_error").html());
					$("#resetPasswordModal div[alertMessage] > div > span")
							.text(data.errorMsg);
				} else {
					app.alertSuccess("重置成功");
					$('#resetPasswordModal').modal('hide');
					// w.location = w.contextPath + "/user";
				}
			}
		},
		"openRegenTokenModal" : function(userId, username) {
			$('#regenTokenModal input[userId]').val(userId);
			$('#regenTokenModal span[username]').text(username);
			$('#regenTokenModal').modal('show');
		},
		"regenToken" : function(form) {
			var userId = $('#regenTokenModal input[userId]').val();
			$("#regenTokenModal div[alertMessage]").text('');
			$.ajax({
				type : $(form).attr('method'),
				url : $(form).attr('action'),
				data : $(form).serialize(),
				dataType : "json",
				success : app.regenTokenDone(userId),
				error : app.httpError
			});
			return false;
		},
		"regenTokenDone" : function(userId) {
			return function(data) {
				if (data.success == false) {
					$("#regenTokenModal div[alertMessage]").html(
							$("#alert_error").html());
					$("#regenTokenModal div[alertMessage] > div > span").text(
							data.errorMsg);
				} else {
					app.alertSuccess("重置Token成功");
					$('#regenTokenModal').modal('hide');
					w.location.reload();
				}
			}
		},
		"openInstanceLogModal" : function(attemptId) {
			app.getAttemptLogTask(attemptId);
			$("#instanceLogModal pre[log]").text("");
			$('#instanceLogModal').modal('show');

			// 自动刷新log
			app["autoGetLog_" + attemptId] = true;
			$('#instanceLogModal').on('hidden.bs.modal', function() {
				app["autoGetLog_" + attemptId] = false;
			});
		},
		"getAttemptLogTask" : function(attemptId) {
			var param = new Object();
			param.attemptId = attemptId;
			$.ajax({
				type : "get",
				url : w.contextPath + "/task/instance/attempt/getLog",
				data : param,
				dataType : "json",
				success : app.getAttemptLogTaskDone(attemptId),
				error : app.httpError
			});
		},
		"getAttemptLogTaskDone" : function(attemptId) {
			return function(data) {
				if (data.success == false) {
					$("#instanceLogModal div[alertMessage]").html(
							$("#alert_error").html());
					$("#instanceLogModal div[alertMessage] > div > span").text(
							data.errorMsg);
				} else {
					if (app["autoGetLog_" + attemptId]) {
						var text = $("#instanceLogModal pre[log]").text();
						if (text == null || text != data.log) {
							$("#instanceLogModal pre[log]").text(data.log);
						}
						if (data.logLink) {
							$("#instanceLogModal a[logLink]").attr("href",
									data.logLink);
						}
						setTimeout(function() {
							app.getAttemptLogTask(attemptId);
						}, 2000);
					}
				}
			}
		},
		// //////////////////////////////////////////////////////////////////
		"openCreateAgentModal" : function() {
			$('#createAgentModal').modal('show');
			$("#createAgentModal input[name='name']").focus();
		},
		"createAgent" : function(form) {
			$("#createAgentModal div[alertMessage]").text('');
			$.ajax({
				type : $(form).attr('method'),
				url : $(form).attr('action'),
				data : $(form).serialize(),
				dataType : "json",
				success : app.createAgentDone,
				error : app.httpError
			});
			return false;
		},
		"createAgentDone" : function(data) {
			if (data.success == false) {
				$("#createAgentModal div[alertMessage]").html(
						$("#alert_error").html());
				$("#createAgentModal div[alertMessage] > div > span").text(
						data.errorMsg);
			} else {
				w.location = w.contextPath + "/agent";
			}
		},
		"openRemoveAgentModal" : function(agentId, agentName) {
			$('#removeAgentModal input[agentId]').val(agentId);
			$('#removeAgentModal span[agentName]').text(agentName);
			$('#removeAgentModal input[agentName]').val(agentName);
			$('#removeAgentModal').modal('show');
		},
		"removeAgent" : function(form) {
			var agentId = $('#removeAgentModal input[agentId]').val();
			$("#removeAgentModal div[alertMessage]").text('');
			$.ajax({
				type : $(form).attr('method'),
				url : $(form).attr('action'),
				data : $(form).serialize(),
				dataType : "json",
				success : app.removeAgentDone(agentId),
				error : app.httpError
			});
			return false;
		},
		"removeAgentDone" : function(agentId) {
			return function(data) {
				if (data.success == false) {
					$("#removeAgentModal div[alertMessage]").html(
							$("#alert_error").html());
					$("#removeAgentModal div[alertMessage] > div > span").text(
							data.errorMsg);
				} else {
					w.location = w.contextPath + "/agent";
				}
			}
		},
		"openUpdateAgentIpModal" : function(agentId, agentName, ip, teamId) {
			$('#updateAgentIpModal input[agentId]').val(agentId);
			$('#updateAgentIpModal span[agentName]').text(agentName);
			$('#updateAgentIpModal input[agentName]').val(agentName);
			$('#updateAgentIpModal input[ip]').val(ip);
			$('#updateAgentIpModal select[teamId]').val(teamId);
			$('#updateAgentIpModal').modal('show');
		},
		"updateAgentIp" : function(form) {
			var agentId = $('#updateAgentIpModal input[agentId]').val();
			$("#updateAgentIpModal div[alertMessage]").text('');
			$.ajax({
				type : $(form).attr('method'),
				url : $(form).attr('action'),
				data : $(form).serialize(),
				dataType : "json",
				success : app.updateAgentIpDone(agentId),
				error : app.httpError
			});
			return false;
		},
		"updateAgentIpDone" : function(agentId) {
			return function(data) {
				if (data.success == false) {
					$("#updateAgentIpModal div[alertMessage]").html(
							$("#alert_error").html());
					$("#updateAgentIpModal div[alertMessage] > div > span")
							.text(data.errorMsg);
				} else {
					w.location = w.contextPath + "/agent";
				}
			}
		},

		"openEnableOrDisableAgentModal" : function(agentId, agentName, enable) {
			$('#enableOrDisableAgentModal input[agentId]').val(agentId);
			$('#enableOrDisableAgentModal span[agentName]').text(agentName);
			$('#enableOrDisableAgentModal input[agentName]').val(agentName);
			$('#enableOrDisableAgentModal input[enable]').val(enable);
			$('#enableOrDisableAgentModal').modal('show');
			if (enable) {
				$('#enableOrDisableAgentModal .enable').show();
				$('#enableOrDisableAgentModal .disable').hide();
			} else {
				$('#enableOrDisableAgentModal .disable').show();
				$('#enableOrDisableAgentModal .enable').hide();
			}
		},
		"enableOrDisableAgent" : function(form) {
			var agentId = $('#enableOrDisableAgentModal input[agentId]').val();
			$("#enableOrDisableAgentModal div[alertMessage]").text('');
			$.ajax({
				type : $(form).attr('method'),
				url : $(form).attr('action'),
				data : $(form).serialize(),
				dataType : "json",
				success : app.enableOrdisableAgentDone(agentId),
				error : app.httpError
			});
			return false;
		},
		"enableOrdisableAgentDone" : function(agentId) {
			return function(data) {
				if (data.success == false) {
					$("#enableOrDisableAgentModal div[alertMessage]").html(
							$("#alert_error").html());
					$("#enableOrDisableAgentModal div[alertMessage] > div > span")
							.text(data.errorMsg);
				} else {
					w.location = w.contextPath + "/agent";
				}
			}
		},

		// //////////////////////////////////////////////////////////////////
		"isEmail" : function(email) {
			var regex = /^([a-zA-Z0-9_\.\-\+])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
			return regex.test(email);
		},
		"refresh" : function() {
			w.location.reload();
		},
		"alertProgress" : function(divId) {
			if (!divId) {
				divId = "alertMessageDiv";
			}
			$("#" + divId).html($("#progress").html());
		},
		"clearAlertMessage" : function(divId) {
			if (!divId) {
				divId = "alertMessageDiv";
			}
			$("#" + divId).html('');
		},
		"alertError" : function(msg, divId) {
			if (!divId) {
				divId = "alertMessageDiv";
			}
			$("#" + divId).html($("#alert_error").html());
			$("#" + divId + " > div > span").text(msg);
		},
		"alertSuccess" : function(msg, divId) {
			// $("#alertMessageDiv").html($("#alert_success").html());
			// $("#alertMessageDiv > div > span > span").text(msg);
			if (!divId) {
				divId = "alertMessageDiv";
			}
			$("#" + divId).html($("#alert_success").html());
			$("#" + divId + " > div > span").text(msg);
		},
		"alertWarn" : function(msg, divId) {
			// $("#alertMessageDiv").html($("#alert_warn").html());
			// $("#alertMessageDiv > div > span > span").text(msg);
			if (!divId) {
				divId = "alertMessageDiv";
			}
			$("#" + divId).html($("#alert_warn").html());
			$("#" + divId + " > div > span").text(msg);
		},
		"appError" : function(title, errorMsg) {
			app.alertErrorModal(title, errorMsg);
		},
		"httpError" : function(xhr, textStatus, errorThrown) {
			app.alertErrorModal('抱歉啦，亲', '抱歉，网络发生错误了，请刷新页面试试...');
		},
		"alertErrorModal" : function(title, errorMsg) {
			// 显示错误消息
			$('#errorMsg h4[title]').text(title);
			$('#errorMsg span[alertMessage]').text(errorMsg);
			$('#errorMsg').modal('show');
		},
		"endWith" : function(s, endStr) {
			if (s == null || s == "" || s.length == 0
					|| endStr.length > s.length)
				return false;
			if (s.substring(s.length - endStr.length) == endStr)
				return true;
			else
				return false;
			return true;
		},
		"startWith" : function(s, preStr) {
			if (s == null || s == "" || s.length == 0
					|| preStr.length > s.length)
				return false;
			if (s.substr(0, preStr.length) == preStr)
				return true;
			else
				return false;
			return true;
		},
		"refresh" : function() {
			w.location.reload();
		},
		"bookmarkPage" : function(url, title) {
			try {
				if (!url) {
					url = window.location
				}
				if (!title) {
					title = document.title
				}
				var browser = navigator.userAgent.toLowerCase();
				if (window.sidebar) { // Mozilla, Firefox, Netscape
					window.sidebar.addPanel(title, url, "");
				} else if (window.external) { // IE or chrome
					if (browser.indexOf('chrome') == -1) { // ie
						window.external.AddFavorite(url, title);
					} else { // chrome
						alert('请按 Ctrl和D 快捷键进行收藏');
					}
				} else if (window.opera && window.print) { // Opera -
					// automatically
					// adds to sidebar if
					// rel=sidebar in the tag
					return true;
				} else if (browser.indexOf('konqueror') != -1) { // Konqueror
					alert('请按 Ctrl和B 快捷键进行收藏');
				} else if (browser.indexOf('webkit') != -1) { // safari
					alert('请按 Ctrl和B 快捷键进行收藏');
				} else {
					alert('您的浏览器不支持该操作，请您点击浏览器的“收藏”菜单进行添加。');
				}
			} catch (err) {
				alert('您的浏览器不支持该操作，请您点击浏览器的“收藏”菜单进行添加。');
			}
		},
		"onHashChange" : function() {
			var hash = window.location.hash;
			if (hash.length > 0) {
				// 去掉#号
				hash = hash.substring(1);
				var j, r;
				$.each(hash.split('&'), function(i, part) {
					var keyValue = part.split('=');
					if (keyValue[0] == 'j') {
						j = keyValue[1];
						rundemo_app.changeJavaCodeFile(keyValue[1]);
					} else if (keyValue[0] == 'r') {
						r = keyValue[1];
						rundemo_app.changeResourceFile(keyValue[1]);
					}
				});
				if (j == 0 && r == 0) {
					window.location.hash = "";
				}
			} else {
				rundemo_app.changeJavaCodeFile(0);
				rundemo_app.changeResourceFile(0);
			}
		},

		"onCrontabTip" : function() {
			var crontabs = {
				"每分钟" : "* * * * *",
				"每十分钟" : "*/10 * * * *",
				"每小时" : "0 * * * *",
				"每天零点" : "0 0 * * *",
				"每周一" : "0 0 * * 2",
				"每月一号" : "0 0 1 * *"
			};
			for ( var key in crontabs) {
				$('#crontab_div').append(
						'[<a onclick = "$(\'#crontab\').val(\'' + crontabs[key]
								+ '\')">' + key + '</a>]&nbsp');
			}
		}
	};
	w.app = app;
}(window || this));

$(document).ready(function() {
	$('a[data-toggle=tooltip]').tooltip();

});

/** 长度超过多少就截断，并加省略号 */
String.prototype.trunc = String.prototype.trunc || function(n) {
	return this.length > n ? this.substr(0, n - 3) + '...' : this;
};
