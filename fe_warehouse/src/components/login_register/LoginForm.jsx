import TextField from "@mui/material/TextField";
import axios from "axios";
import { setUser } from "../../redux/reducers/authSlice";
import { useCookies } from "react-cookie";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import { useState } from "react";

function LoginForm() {
  const [cookie, setCookie] = useCookies();
  const dispatch = useDispatch();
  const [form, setForm] = useState({});
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleChangeForm = (e) => {
    const { name, value } = e.target;
    setForm({ ...form, [name]: value });
  };

  const handleLogin = async (e) => {
    e.preventDefault();
    await axios
      .post("http://localhost:8080/api/v1/sign/sign-in", form)
      .then((resp) => {
        dispatch(setUser(resp.data));
        setCookie("role", resp.data.roles[0], { path: '/', maxAge: 86400000 })
        setCookie("fullName", formatFullName(resp.data.firstName + " " + resp.data.lastName), { path: '/', maxAge: 86400000 })
        setCookie("token", resp.data.token, { path: "/", maxAge: 86400000 });
        localStorage.setItem("user", JSON.stringify(resp.data));
        localStorage.setItem("active", JSON.stringify("Tổng Quan"))
        if (resp.data.roles.includes("ROLE_ADMIN")) {
          navigate("/admin");
        }
        if (resp.data.roles.includes("ROLE_MANAGER")) {
          navigate("/manager");
        }
        if (resp.data.roles.includes("ROLE_SUPERMARKET")) {
          navigate("/market");
        }
        setError("");
      })
      .catch((err) => {
        console.log(err)
        setError(err.response.data);
      });
  };

  const formatFullName = (text) => {
    let arr = text.split(" ");
    let newArr = [];
    for (let i = 0; i < arr.length; i++) {
      newArr.push(arr[i].charAt(0).toUpperCase() + arr[i].slice(1));
    }
    return newArr.join(" ");
  }

  return (
    <div style={{ display: "flex", background: "#ecf0f1", borderRadius: "12px" }} className="shadow-2xl">
      <div style={{ width: "600px", textAlign: "center", padding: "50px 20px" }}>
        <img
          src="logo.png"
          alt=""
          className="h-24 w-52 object-cover block mx-auto"
        />
        <h2>Đăng nhập vào nhà kho của bạn</h2>
        <form
          onSubmit={handleLogin}
          action=""
          style={{
            width: "80%",
            padding: "20px",
            margin: "0 auto",
            display: "flex",
            gap: "20px",
            flexDirection: "column",
          }}
        >
          {error && <p style={{ color: "red", fontSize: "20px" }}>{error}</p>}
          <TextField
            onChange={handleChangeForm}
            name="username"
            id="outlined-username"
            size="small"
            label="Email"
            variant="outlined"
          />
          <TextField
            onChange={handleChangeForm}
            name="password"
            id="outlined-password"
            size="small"
            label="Password"
            type="password"
            variant="outlined"
          />

          <button
            type="submit"
            className="p-2 bg-cyan-400 text-white w-52 rounded-full block mx-auto"
          >
            Đăng Nhập
          </button>
        </form>
      </div>
      <div>
        <img
          src="image_form.jpg"
          alt=""
          className="w-80 h-full object-cover rounded-r-xl"
        />
      </div>
    </div>
  );
}

export default LoginForm;
