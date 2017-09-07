//存放主要的交互逻辑的js代码
//javascript模块化（包.类.方法名）
//seckill.detail.init(params)
var seckill = {

    //1.封装秒杀相关ajax的url
    URL: {
        //注意这里的调用的是访问地址，http://127.0.0.1:8080/time/now
        //要与后端控制器里提供的地址一致，我自己控制器里访问地址是不带seckill的
        //之前报404错误就是没找到该地址，地址写错了，写成了/seckill/time/now，多了一个seckill

        //部署到服务器上时，这里的路径要写../time/now，不能写成/time/now
        //否则会找不到路径，貌似js里的路径和有关js的路径都要这样写！！！！
        now: function () {
            return '../time/now';
        },
        //暴露秒杀地址
        exposer: function (seckillId) {
            return '../' + seckillId + '/exposer';
        },
        //执行秒杀
        execution: function (seckillId, md5) {
            return '../' + seckillId + '/' + md5 + '/execution';
        }
    },

    //2.验证手机号
    validatePhone: function (phone) {
        //直接判断对象会看对象是否为空,空就是undefine就是false; isNaN 非数字返回true
        if (phone && phone.length == 11 && !isNaN(phone)) {
            return true;
        } else {
            return false;
        }
    },

    //3.详情页秒杀逻辑
    detail: {
        //详情页初始化
        init: function (params) {
            //手机验证和登录,计时交互
            //规划我们的交互流程
            //在cookie中查找手机号
            var userPhone = $.cookie('userPhone');
            //验证手机号
            if (!seckill.validatePhone(userPhone)) {
                //绑定手机 控制输出
                var killPhoneModal = $('#killPhoneModal');
                killPhoneModal.modal({
                    show: true,//显示弹出层
                    backdrop: false,//禁止位置关闭
                    keyboard: false//关闭键盘事件
                });

                $('#killPhoneBtn').click(function () {
                    var inputPhone = $('#killPhoneKey').val();
                    console.log("inputPhone: " + inputPhone);
                    if (seckill.validatePhone(inputPhone)) {
                        //电话写入cookie(1天过期)

                        $.cookie('userPhone', inputPhone, {expires: 1});
                        //验证通过　　刷新页面
                        window.location.reload();
                    } else {
                        //todo 错误文案信息抽取到前端字典里
                        $('#killPhoneMessage').hide().html('<label class="label label-danger">手机号错误!</label>').show(300);
                    }
                });
            }
        }
    }




}