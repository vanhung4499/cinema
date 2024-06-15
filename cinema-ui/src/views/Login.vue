<template>
  <div class="login_container">
    <div class="login_box">
      <div class="title_box">
        <p>Login</p>
      </div>
      <!-- Login form area -->
      <el-form class="login-form" :model="loginForm" :rules="loginFormRules" ref="loginFormRef">
        <!-- Username -->
        <el-form-item prop="userName">
          <el-input v-model="loginForm.userName" placeholder="Please enter username" clearable
                    prefix-icon="iconfont icon-user"></el-input>
        </el-form-item>
        <!-- Password -->
        <el-form-item prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="Please enter password" show-password
                    prefix-icon="iconfont icon-lock"></el-input>
        </el-form-item>
        <!-- Button area -->
        <el-form-item class="btns">
          <el-button size="medium" :round="true" type="primary" @click="login">Login</el-button>
          <el-button size="medium" :round="true" type="info" @click="resetLoginForm">Reset</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      // Login form data object
      loginForm: {
        userName: 'admin',
        password: '123456'
      },
      // Form validation rules
      loginFormRules: {
        // Validate username
        userName: [
          { required: true, message: "Please enter username", trigger: "blur"},
          { min: 2, max: 20, message: "Length should be between 2 and 20 characters", trigger: "blur"}
        ],
        // Validate password
        password: [
          { required: true, message: "Please enter password", trigger: "blur"},
          { min: 6, max: 16, message: "Length should be between 6 and 16 characters", trigger: "blur"}
        ]
      }
    };
  },
  methods:{
    success(params) {
      console.log(params);
      this.login()
    },
// Clicking the reset button to reset the form
    resetLoginForm(){
      console.log(this.$refs);
      this.$refs.loginFormRef.resetFields();
    },

// Method for handling login
    login() {
      // Validate the form
      this.$refs.loginFormRef.validate(async valid => {
        if (!valid) return;

        // Set default headers for axios
        axios.defaults.headers.post['Content-Type'] = 'application/json';

        // Send login request
        const { data: res } = await axios.post('sysUser/login', JSON.stringify(this.loginForm));

        // Handle response
        if (res.code !== 200) return this.$message.error(res.msg);

        // Control login permissions
        if (res.data.sysUser.sysRole.children === null || res.data.sysUser.sysRole.children[0] === null) {
          this.$message.error("Sorry, you do not have permission to log in. Please contact the administrator for access.");
          return;
        }

        // Display success message
        this.$message.success("Login successful");

        // Save token and user info in sessionStorage
        window.sessionStorage.setItem("token", res.data.token);
        window.sessionStorage.setItem("loginUser", JSON.stringify({ sysUser: res.data.sysUser, cinemaId: res.data.cinemaId, cinemaName: res.data.cinemaName }));

        // Set button permissions based on user role
        window.sessionStorage.setItem("btnPermission", res.data.sysUser.sysRole.roleId === 1 ? "admin" : "admin"); // Adjust based on actual logic

        // Navigate to the welcome page
        await this.$router.push('/welcome');
      });
    }
  }
}
</script>

<style scoped>
.login_container{
  background-image: url("../assets/login-background.jpg");
  height: 100%;
}

.login_box{
  width: 450px;
  height: 300px;
  background-color: #fff;
  border-radius: 3px;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
}

.avatar_box{
  height: 130px;
  width: 130px;
  border: 1px solid #eee;
  border-radius: 50%;
  padding: 10px;
  box-shadow: 0 0 10px #ddd;
  position: absolute;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: #fff;
}

.avatar_box > img{
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background-color: #eee;
}

.title_box{
  text-align: center;
  font-size: 200%;
}

.login-form{
  position: absolute;
  bottom: 0;
  width: 100%;
  padding: 0 20px;
  box-sizing: border-box;
}

.btns{
  display: flex;
  justify-content: center;
}
</style>
