import { Cookies } from "react-cookie";
import { createAsyncThunk } from "@reduxjs/toolkit";
import instance from "..";

export const GET_ALL_ZONE = createAsyncThunk("zone/GETALLZONE", async () => {
  return await instance.get("/api/v1/zone", {
    headers: {
      Authorization: `Bearer ${new Cookies().get("token")}`
    }
  }).then(resp => resp.data);
})