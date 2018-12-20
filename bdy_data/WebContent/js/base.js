
function sh(x){
	if($("#"+x).is(":hidden")){
		$("#"+x).show();
	}
	else{
		$("#"+x).hide();
	}
 } 
function fv(str){
	if(str==1){
		$('#st').html("商标名");
	}
	else if(str==2){
		$('#st').html("注册号");
	}
	else if(str==3){
		$('#st').html("申请人");
	}
	else if(str==4){
		$('#st').html("不限条件");
	}
	$("#searchType").val(str);
	sh('hh');
}

function goSearch(){
	   if($("#topsearchkey").val() != ""){
			$("#topForm").submit();
	   }
}
 function goSearchIdx(){
    if($("#idxsearchkey").val()!= null && $("#idxsearchkey").val()!= ""){
		$("#idxForm").submit();
    }
}
 
 function goSearchTm(){
	    if($("#custname").val()!= null && $("#custname").val()!= ""){
	    	if($("#appname").val()!= null && $("#appname").val()!= "" && $("#appname").val()!="/r/n"){
	 			$("#idxForm").submit();
	 	    }else{
	 	    	alert('申请人不能为空');
	 	    }			
	    }else{
	    	alert('客户名称不能为空');
	    }
	}

String.prototype.trim=function(){return this.replace(/(^\s*)|(\s*$)/g,"")};

function TMRGet(a,c,b){if(a.indexOf("?")>0){a+="&t="+Math.random()}else{a+="?t="+Math.random()}$.ajax({async:false,type:"get",url:a,data:null,timeout:90000,dataType:c,success:b,error:function(){art.dialog.tips("失败，请重试",1)}})}
function TMRAsynGet(a,c,b){if(a.indexOf("?")>0){a+="&t="+Math.random()}else{a+="?t="+Math.random()}$.ajax({type:"get",url:a,timeout:90000,data:null,dataType:c,success:b,error:function(){art.dialog.tips("失败，请重试",1)}})}
function TMRPost(a,c,e,d){var b=$("#"+c).serialize();$.ajax({async:false,type:"POST",url:a,timeout:90000,data:b,dataType:e,success:d,error:function(){art.dialog.tips("失败，请重试",1)}})}function TMRAsynPost(a,c,e,d){var b=$("#"+c).serialize();$.ajax({type:"POST",url:a,timeout:90000,data:b,dataType:e,success:d,error:function(){art.dialog.tips("失败，请重试",1)}})}var afterLoginUrl;
function TMRVerifyLogin(a){$.ajax({type:"POST",url:"/user!hasLogin.php",timeout:90000,data:null,dataType:"text",async:false,success:function(c){if(c=="SUCCESS"){location.href=a}else{afterLoginUrl=a;var b={title:"请登录",lock:true};art.dialog.open("/login.php?act=url",b,false)}},error:function(){art.dialog.tips("失败，请重试",1)}})}
