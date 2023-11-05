import { Cookies } from "react-cookie";
import { createAsyncThunk } from "@reduxjs/toolkit";
import instance from "..";

export const GET_ALL_INVENTORY_BY_STORAGE_ID = createAsyncThunk("inventory/GET_ALL_INVENTORY_BY_STORAGE_ID", async ({ search, id }) => {
  return await instance.get(`/api/v1/inventory/inventory-storage/${id}/?note=${search}`, {
    headers: {
      Authorization: `Bearer ${new Cookies().get('token')}`
    }
  }).then(resp => resp.data);
})

export const ADD_INVENTORY = createAsyncThunk('inventory/ADD_INVENTORY', async ({ formData, id }) => {
  return await instance.post(`/api/v1/inventory/importInventoryReportExcel/${id}`, formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
      Authorization: `Bearer ${new Cookies().get('token')}`
    }
  }).then(resp => resp.data);
});