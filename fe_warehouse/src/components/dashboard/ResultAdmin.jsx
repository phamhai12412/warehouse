import { DATA_BILL_REMOTE, DATA_PRODUCT, DATA_USER } from "../../redux/selectors/selectors";
import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";

import DescriptionIcon from "@mui/icons-material/Description";
import { GET_ALL_BILL } from "../../redux/api/service/billService";
import { GET_ALL_PRODUCT } from "../../redux/api/service/productService";
import { GET_ALL_USER } from "../../redux/api/service/userService";
import Groups2Icon from "@mui/icons-material/Groups2";
import PaidIcon from "@mui/icons-material/Paid";
import WidgetsIcon from "@mui/icons-material/Widgets";
import instance from "../../redux/api";

function ResultAdmin({ reload }) {

  const users = useSelector(DATA_USER);
  const products = useSelector(DATA_PRODUCT);
  const bills = useSelector(DATA_BILL_REMOTE);
  const dispatch = useDispatch();

  const [revenue, setRevenue] = useState(null);
  const handleLoadRevenue = () => {
    instance.get(`/api/v1/dashboard/admin/GVCSAndByYear`)
      .then((resp) => setRevenue(resp.data))
      .catch((err) => console.log(err));
  }

  useEffect(() => {
    handleLoadRevenue();
    dispatch(GET_ALL_USER());
    dispatch(GET_ALL_PRODUCT());
    dispatch(GET_ALL_BILL());
  }, [reload])

  return (
    <div style={{ background: "#fff" }}>
      <div className="p-4 flex justify-between items-center border-b-2 border-slate-300">
        <h2 className="font-semibold text-2xl">Kết Quả Doanh Thu</h2>
      </div>
      <div className="content flex">
        <div className="flex items-center p-6 flex-1 hover:bg-slate-200 transition-all">
          <PaidIcon sx={{ fontSize: "45px" }} />
          <div className="body pl-4">
            <p className="text-xl font-semibold">Doanh Thu</p>
            <p className="font-normal">{revenue && revenue.toLocaleString()} VNĐ</p>
          </div>
        </div>
        <div className="flex items-center p-6 flex-1 hover:bg-slate-200 transition-all">
          <DescriptionIcon sx={{ fontSize: "45px" }} />
          <div className="body pl-4">
            <p className="text-xl font-semibold">Đơn Hàng</p>
            <p className="font-normal">{bills && bills.length}</p>
          </div>
        </div>
        <div className="flex items-center p-6 flex-1 hover:bg-slate-200 transition-all">
          <WidgetsIcon sx={{ fontSize: "45px" }} />
          <div className="body pl-4">
            <p className="text-xl font-semibold">Sản Phẩm</p>
            <p className="font-normal">{products && products.reduce((sum, item) => (sum += item?.quantity), 0)}</p>
          </div>
        </div>
        <div className="flex items-center p-6 flex-1 hover:bg-slate-200 transition-all">
          <Groups2Icon sx={{ fontSize: "45px" }} />
          <div className="body pl-4">
            <p className="text-xl font-semibold">Nhân Viên</p>
            <p className="font-normal">{users && users.length}</p>
          </div>
        </div>
      </div>
    </div>
  );
}

export default ResultAdmin;
