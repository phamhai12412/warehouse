import { Cookies } from "react-cookie";
import { createAsyncThunk } from "@reduxjs/toolkit";
import instance from "..";

export const GET_ALL_BILL_EXPORT_BY_STORAGE_ID = createAsyncThunk("billExport/GET_ALL_BILL_EXPORT_BY_STORAGE_ID", async ({ delivery, search, id }) => {
  return instance.get(`/api/v1/shipping-report/export-storage/${id}/?statusName=${delivery}&search=${search}`, {
    headers: {
      Authorization: `Bearer ${new Cookies().get("token")}`
    }
  }).then(resp => resp.data)
}) 
