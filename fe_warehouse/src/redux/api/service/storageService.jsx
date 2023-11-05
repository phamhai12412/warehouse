import { Cookies } from "react-cookie";
import { createAsyncThunk } from "@reduxjs/toolkit";
import instance from "..";

export const GET_ALL_STORAGE = createAsyncThunk("storage/GETALLSTORAGE", async (search) => {
  if (search) {
    return await instance.get(`/api/v1/storage/?name=${search}`, {
      headers: {
        Authorization: `Bearer ${new Cookies().get("token")}`
      }
    }).then((resp) => resp.data).catch(err => console.log(err.response));
  }
  return await instance.get("/api/v1/storage", {
    headers: {
      Authorization: `Bearer ${new Cookies().get("token")}`
    }
  }).then((resp) => resp.data).catch(err => console.log(err.response));
})

