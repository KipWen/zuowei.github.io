<template>
  <div class="body-bg">

    <el-form ref="ruleFormRef" style="max-width: 600px" :model="loginForm" status-icon 
    :rules="rules" label-width="auto" class="loginFormContainer">
        <h1 class="loginTitle">系统登入</h1>
        <el-form-item label="用户名" prop="username">
            <el-input v-model="loginForm.username" type="text" autocomplete="off" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
            <el-input v-model="loginForm.password" type="password" autocomplete="off" />
        </el-form-item>
        <el-form-item label="验证码" prop="checkCode" class="checkCodeItem">
            <el-input v-model="loginForm.checkCode" type="text" autocomplete="off"  />
        </el-form-item>
        <img :src="checkCodeImgURL" class="checkCodeImg"  @click="changCheckCodeImg()"/>
        <el-form-item>
            <el-button  type="primary" @click="submitForm(ruleFormRef)" style="width: 47%;" >
                Submit
            </el-button>
            <el-button @click="resetForm(ruleFormRef)" style="width: 47%;" >
                Reset
            </el-button>
        </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts" setup>
    import { reactive, ref } from 'vue'
    import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
    import { postRequest } from '@/util/api';
    import { useRouter } from 'vue-router';
    const loginForm = ref({
        username: "",
        password: "",
        checkCode:""
    })

    //定义验证码路径
    const checkCodeImgURL = ref('/api/getCodeImg');
    const changCheckCodeImg = () => {
        checkCodeImgURL.value = "/api/getCodeImg?time=" + new Date();
    }

    const ruleFormRef = ref<FormInstance>()

    const resetForm = (formEl: FormInstance | undefined) => {
        if (!formEl) return
        formEl.resetFields()
    }

    const ruleForm = reactive({
        username: '',
        password: '',
        checkCode: '',
    })

    const validateEmpty = (rule: any, value: any, callback: any) => {
        if (value === '') {
            callback(new Error('请输入有效数据'))
        } 
        callback()
    }

    const rules = reactive<FormRules<typeof ruleForm>>({
        username: [{ validator: validateEmpty, trigger: 'blur' }],
        password: [{ validator: validateEmpty, trigger: 'blur' }],
        checkCode: [{ validator: validateEmpty, trigger: 'blur' }],
    })



    const successMsg = (tip) => {
        ElMessage({
            message: tip,
            type: 'success',
        })
    }
    const wrongMsg = (tip) => {
        ElMessage.error(tip);
    }


    const router = useRouter();

    const submitForm = (formEl: FormInstance | undefined) => {
        if (!formEl) return
        formEl.validate((valid) => {
            if (valid) {
                

                postRequest("/login", loginForm.value).then(res => {
                    console.log(res);
                    console.log(res.data.data);
                    console.log(res.data.data  == "success");
                    if(res.data.data == "success"){
                        successMsg(res.data.msg);
                        router.replace("/home")
                    }else{
                        wrongMsg(res.data.msg)
                    }
                }, err => {
                    console.log(err)
                })


            } else {
                console.log('error submit!')
            }
        })
    }


</script>

<style scoped>
.body-bg{
    position: absolute;
    width: 100%;
    height: 100%;
    background-image: url(../assets/back.jpeg);
    background-size: 100% 100%;
}


.loginFormContainer{
    width: 330px;
    background: #fff;
    padding: 5px 35px 15px 35px;
    margin: 70px auto;
    border: 1px solid #eaeaea;
    border-radius: 15px;
    box-shadow: 0 0 25px #eaeaea;
}

.loginTitle{
    text-align: center;
    line-height: 60px;
}

.el-form-item {
    width: 250px;
}

.checkCodeItem{
    width: 140px;
    float: left;
    margin-right: 10px;
}

.checkCodeImg{
    width: 90px;
}

</style>
