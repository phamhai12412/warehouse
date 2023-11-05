import { DATA_BILL_EXPORT, DATA_BILL_IMPORT, DATA_PRODUCT } from "../../redux/selectors/selectors";
import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";

import DescriptionIcon from "@mui/icons-material/Description";
import FormControl from "@mui/material/FormControl";
import { GET_ALL_BILL_EXPORT_BY_STORAGE_ID } from "../../redux/api/service/billExportService";
import { GET_ALL_BILL_IMPORT_BY_STORAGE_ID } from "../../redux/api/service/billImportService";
import { GET_ALL_PRODUCT_BY_STORAGE_ID } from "../../redux/api/service/productService";
import InputLabel from "@mui/material/InputLabel";
import NativeSelect from "@mui/material/NativeSelect";
import PaidIcon from "@mui/icons-material/Paid";
import WidgetsIcon from "@mui/icons-material/Widgets";
import instance from "../../redux/api";

function ResultMarket() {

  const products = useSelector(DATA_PRODUCT);
  const billExport = useSelector(DATA_BILL_EXPORT);
  const billImport = useSelector(DATA_BILL_IMPORT);
  const dispatch = useDispatch();
  const [user, setUser] = useState(JSON.parse(localStorage.getItem('user')));


  const [revenue, setRevenue] = useState(null);
  const handleLoadRevenue = () => {
    instance.get(`/api/v1/dashboard/storage/GVCSAndByYear/${user.storageId}`)
      .then((resp) => setRevenue(resp.data))
      .catch((err) => console.log(err));
  }

  useEffect(() => {
    if (user) {
      handleLoadRevenue();
      dispatch(GET_ALL_PRODUCT_BY_STORAGE_ID({ search: '', filter: 'ALL', id: user.storageId }));
      dispatch(GET_ALL_BILL_EXPORT_BY_STORAGE_ID({ delivery: 'ALL', search: '', id: user.storageId }));
      dispatch(GET_ALL_BILL_IMPORT_BY_STORAGE_ID({ delivery: 'ALL', search: '', id: user.storageId }));
    }
  }, [])

  return (
    <div style={{ background: "#fff" }}>
      <div className="p-3 flex justify-between items-center border-b-2 border-slate-300">
        <h2 className="font-semibold text-2xl">Kết Quả Doanh Thu</h2>
        <FormControl sx={{ width: "200px" }}>
          <InputLabel variant="standard" htmlFor="uncontrolled-native"> Type </InputLabel>
          <NativeSelect defaultValue={"day"}
            inputProps={{
              name: "type",
              id: "uncontrolled-native",
            }}
          >
            <option value={"day"}>Trong Ngày</option>
            <option value={"month"}>Trong Tháng</option>
            <option value={"year"}>Trong Năm</option>
          </NativeSelect>
        </FormControl>
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
          <WidgetsIcon sx={{ fontSize: "45px" }} />
          <div className="body pl-4">
            <p className="text-xl font-semibold">Sản Phẩm</p>
            <p className="font-normal">{products.reduce((sum, item) => sum += item.quantity, 0)}</p>
          </div>
        </div>
        <div className="flex items-center p-6 flex-1 hover:bg-slate-200 transition-all">
          <DescriptionIcon sx={{ fontSize: "45px" }} />
          <div className="body pl-4">
            <p className="text-xl font-semibold">Đơn Nhập</p>
            <p className="font-normal">{billImport.length}</p>
          </div>
        </div>
        <div className="flex items-center p-6 flex-1 hover:bg-slate-200 transition-all">
          <DescriptionIcon sx={{ fontSize: "45px" }} />
          <div className="body pl-4">
            <p className="text-xl font-semibold">Đơn Xuất</p>
            <p className="font-normal">{billExport.length}</p>
          </div>
        </div>
      </div>
    </div>
  )
}

export default ResultMarket