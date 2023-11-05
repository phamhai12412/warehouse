import "./style.css";

import { Link, NavLink, useNavigate } from "react-router-dom";
import React, { useEffect, useState } from "react";

import Accordion from "@mui/material/Accordion";
import AccordionDetails from "@mui/material/AccordionDetails";
import AccordionSummary from "@mui/material/AccordionSummary";
import BarChartOutlinedIcon from "@mui/icons-material/BarChartOutlined";
import { Cookies } from "react-cookie";
import DashboardOutlinedIcon from "@mui/icons-material/DashboardOutlined";
import DescriptionOutlinedIcon from "@mui/icons-material/DescriptionOutlined";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import GroupOutlinedIcon from "@mui/icons-material/GroupOutlined";
import HomeOutlinedIcon from "@mui/icons-material/HomeOutlined";
import LocalShippingOutlinedIcon from "@mui/icons-material/LocalShippingOutlined";
import WidgetsOutlinedIcon from "@mui/icons-material/WidgetsOutlined";

function SidebarAdmin({ toggle, handleToggleNavbar, expanded, handleChange }) {
  const link = [
    {
      id: 1,
      icon: <DescriptionOutlinedIcon className="nav_icon" />,
      subMenu: [
        { id: 1, path: "/admin/bills", text: "Danh Sách Đơn" },
      ],
      text: "Đơn Hàng",
    },
    {
      id: 2,
      icon: <WidgetsOutlinedIcon className="nav_icon" />,
      subMenu: [
        { id: 1, path: "/admin/category", text: "Thể Loại" },
        { id: 2, path: "/admin/product", text: "Sản Phẩm Trong kho" }
      ],
      text: "Sản Phẩm",
    },
    {
      id: 4,
      icon: <LocalShippingOutlinedIcon className="nav_icon" />,
      subMenu: [{ id: 1, path: "/admin/shipper", text: "Dịnh Vụ Giao Hàng" }],
      text: "Đơn Vị Vận Chuyển",
    },
    {
      id: 5,
      icon: <GroupOutlinedIcon className="nav_icon" />,
      subMenu: [
        { id: 1, path: "/admin/account/manager", text: "Tài Khoản Kho" },
        {
          id: 2,
          path: "/admin/account/supermarket",
          text: "Tài Khoản Siêu thị",
        },
      ],
      text: "Nhân Viên",
    },
    {
      id: 6,
      icon: <BarChartOutlinedIcon className="nav_icon" />,
      subMenu: [
        { id: 1, path: "/admin/reports/inventory", text: "Báo Cáo Tồn Kho" },
        { id: 2, path: "/admin/reports/bill", text: "Báo Cáo Đơn Hàng" },
        { id: 3, path: "/admin/reports/revenue", text: "Báo Cáo Doanh Số" },
      ],
      text: "Báo Cáo",
    },
  ];

  const [active, setActive] = useState(JSON.parse(localStorage.getItem("active")));

  const handleActiveSideBar = (text) => {
    setActive(text);
    localStorage.setItem("active", JSON.stringify(text));
  }

  const navigate = useNavigate();

  useEffect(() => {
    if (!(new Cookies().get("role") === "ROLE_ADMIN")) {
      navigate("/");
    }
  }, [])

  return (
    <div className="l-navbar">
      <div className="nav" style={{ width: toggle ? "55px" : "260px" }}>
        <div>
          <div className="navbar__logo">
            {toggle ? (
              <HomeOutlinedIcon className="nav_icon" />
            ) : (
              <Link to={"/admin"}>
                <HomeOutlinedIcon className="nav_icon" /> TỔNG KHO
              </Link>
            )}
          </div>
          {toggle ? (
            <i
              className="fa-solid fa-angle-right nav_toggle_icon flex justify-center items-center"
              onClick={handleToggleNavbar}
            ></i>
          ) : (
            <i
              className="fa-solid fa-angle-left nav_toggle_icon flex justify-center items-center"
              onClick={handleToggleNavbar}
            ></i>
          )}
          <div className="nav_toggle">
            <ul className="nav__list">
              {
                active === "Tổng Quan" ?
                  <Link to={"/admin"} onClick={() => handleActiveSideBar("Tổng Quan")} className="nav__link active">
                    <DashboardOutlinedIcon className="nav_icon" />{" "}
                    {toggle ? "" : "Tổng Quan"}
                  </Link>
                  :
                  <Link to={"/admin"} onClick={() => handleActiveSideBar("Tổng Quan")} className="nav__link">
                    <DashboardOutlinedIcon className="nav_icon" />{" "}
                    {toggle ? "" : "Tổng Quan"}
                  </Link>
              }
              {link.map((item, index) =>
                toggle ? (
                  <div className="nav__link">{item.icon}</div>
                ) : (
                  <Accordion
                    key={item.id}
                    sx={{ bgcolor: "#13192c", color: "#ededed" }}
                    expanded={active === item.text || expanded === `panel${index}`}
                    disableGutters={true}
                    onChange={handleChange(`panel${index}`)}
                  >
                    <AccordionSummary
                      expandIcon={<ExpandMoreIcon sx={{ color: "#ededed" }} />}
                      aria-controls="panel1bh-content"
                      id="panel1bh-header"
                      sx={{ borderRadius: '8px', marginBottom: '3px' }}
                      className={active === item.text ? 'link_header active' : ''}
                    >
                      {item.icon}
                      {item.text}
                    </AccordionSummary>
                    <AccordionDetails>
                      {item.subMenu &&
                        item.subMenu.map((subItem) => (
                          <NavLink
                            key={subItem.id}
                            to={subItem.path}
                            className="sub_link"
                            onClick={() => handleActiveSideBar(item.text)}
                          >
                            <span>{subItem.text}</span>
                          </NavLink>
                        ))}
                    </AccordionDetails>
                  </Accordion>
                )
              )}
            </ul>
          </div>
        </div>
      </div>
    </div >
  );
}

export default SidebarAdmin;
