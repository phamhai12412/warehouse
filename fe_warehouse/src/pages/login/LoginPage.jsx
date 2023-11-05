import LoginForm from "../../components/login_register/LoginForm";

function LoginPage() {
  return (
    <div className="flex justify-center items-center h-screen relative">
      <LoginForm />
      <img src="background_login.svg" alt="" className="absolute bottom-0 w-screen -z-10" />
    </div>
  );
}

export default LoginPage;
