import { GET_ALL_BILL_EXPORT_BY_STORAGE_ID } from "../api/service/billExportService";
import { createSlice } from "@reduxjs/toolkit";

const billExportSlice = createSlice({
  name: 'billExport',
  initialState: [],
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(GET_ALL_BILL_EXPORT_BY_STORAGE_ID.fulfilled, (state, action) => {
        return action.payload.content;
      })
  }
})

export const { } = billExportSlice.actions;
export default billExportSlice.reducer;