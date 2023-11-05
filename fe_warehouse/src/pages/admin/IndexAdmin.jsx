import React, { useState } from "react";

import Navbar from "../../components/navbar/Navbar";
import { Outlet } from "react-router-dom";
import SidebarAdmin from "../../components/sidebar/SidebarAdmin";

function IndexAdmin() {
  const [toggle, setToggle] = useState(false);
  const [expanded, setExpanded] = useState(false);

  const handleToggleNavbar = (e) => {
    setToggle(!toggle);
    setExpanded(false);
  };

  const handleChange = (panel) => (event, isExpanded) => {
    setExpanded(isExpanded ? panel : false);
  };

  return (
    <div>
      <SidebarAdmin
        toggle={toggle}
        handleToggleNavbar={handleToggleNavbar}
        expanded={expanded}
        handleChange={handleChange}
      />
      <div style={{ marginLeft: toggle ? "70px" : "275px" }}>
        <Navbar />
        <div style={{ padding: '24px', background: "#dfe6e9", height: 'calc( 100vh - 55px )', maxHeight: "calc( 100vh - 55px )", overflowX: 'hidden', overflowY: 'auto' }}>
          <Outlet />
        </div>
      </div>
    </div>
  );
}

export default IndexAdmin;
