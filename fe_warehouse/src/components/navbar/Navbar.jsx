import { Cookies, useCookies } from "react-cookie";
import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";

import { Avatar } from "@mui/material";
import Chip from "@mui/material/Chip";
import { DATA_LOGIN } from "../../redux/selectors/selectors";
import FormChangePassword from "../form/FormChangePassword";
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import LogoutIcon from '@mui/icons-material/Logout';
import Menu from "@mui/material/Menu";
import MenuItem from "@mui/material/MenuItem";
import Person2OutlinedIcon from '@mui/icons-material/Person2Outlined';
import { resetUser } from "../../redux/reducers/authSlice";
import { useNavigate } from "react-router-dom";

function Navbar() {
  const [user, setUser] = useState(JSON.parse(localStorage.getItem('user')));
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const [cookies, setCookie, removeCookie] = useCookies(['token']);

  const [anchorEl, setAnchorEl] = React.useState(null);
  const open = Boolean(anchorEl);
  const handleShowMenu = (event) => {
    setAnchorEl(event.currentTarget);
  };
  const handleClose = () => {
    setAnchorEl(null);
  };

  const handleInformation = () => {
    if (new Cookies().get('role') === "ROLE_MANAGER") {
      navigate(`/manager/information`);
    } else {
      navigate(`/market/information`);
    }

    handleClose();
  }

  const handleLogout = () => {
    dispatch(resetUser());
    removeCookie("token", { path: '/' })
    removeCookie("fullName", { path: '/' })
    removeCookie("role", { path: '/' })
    localStorage.removeItem("user");
    localStorage.removeItem("active");
    navigate("/");
  };

  const [openChangePassword, setOpenChangePassword] = React.useState(false);
  const handleOpenChangePassword = () => { setOpenChangePassword(true); handleClose(); }
  const handleCloseChangePassword = () => setOpenChangePassword(false);

  useEffect(() => {
    if (!(new Cookies().get("fullName"))) {
      navigate("/");
    }
  }, [])

  return (
    <div
      style={{
        display: "flex",
        justifyContent: "flex-end",
        alignItems: "center",
        height: "55px",
        padding: "0 15px",
        position: 'sticky',
        top: '0',
        zIndex: '10',
        background: '#fff'
      }}
      className="shadow-xl"
    >
      <Chip
        sx={{ padding: "20px 5px", fontSize: '18px', minWidth: "150px", borderRadius: '999px' }}
        label={new Cookies().get("fullName")}
        color="primary"
        component="a"
        clickable
        onClick={handleShowMenu}
      />
      <Menu
        id="basic-menu"
        anchorEl={anchorEl}
        open={open}
        onClose={handleClose}
        MenuListProps={{
          "aria-labelledby": "basic-button",
        }}
        sx={{
          marginTop: "10px",
          ".MuiList-root": {
            width: "150px",
            padding: "0",
          },
        }}
      >
        {new Cookies().get('role') !== "ROLE_ADMIN" ?
          <MenuItem className="flex gap-2" onClick={handleInformation}><Person2OutlinedIcon /> Thông tin</MenuItem> :
          ''
        }
        <MenuItem className="flex gap-2" onClick={handleOpenChangePassword}><LockOutlinedIcon /> Password</MenuItem>
        <MenuItem className="flex gap-2" onClick={handleLogout}><LogoutIcon /> Log out</MenuItem>
      </Menu>
      {openChangePassword &&
        <FormChangePassword
          open={openChangePassword}
          handleClose={handleCloseChangePassword}
          handleLogout={handleLogout}
        />
      }
    </div>
  );
}

export default Navbar;
