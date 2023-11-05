import React, { useState } from "react";

import BillPendingAdmin from "../../components/dashboard/BillPendingAdmin";
import Calender from "../../components/dashboard/Calender";
import ProductPendingAdmin from "../../components/dashboard/ProductPendingAdmin";
import ResultAdmin from "../../components/dashboard/ResultAdmin";

function DashBoardAdmin() {

  const [reload, setReload] = useState(false);
  const handleReload = () => setReload(!reload);

  return (
    <div className="flex">
      <div className="p-7 flex flex-col gap-7" style={{ width: "75%" }}>
        <ResultAdmin reload={reload} />
        <BillPendingAdmin reload={reload} handleReload={handleReload} />
        <ProductPendingAdmin reload={reload} handleReload={handleReload} />
      </div>
      <div className="p-7 flex-1">
        <Calender />
      </div>
    </div>
  );
}

export default DashBoardAdmin;
