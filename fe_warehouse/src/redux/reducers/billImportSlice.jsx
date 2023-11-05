import { GET_ALL_BILL_IMPORT_BY_STORAGE_ID } from "../api/service/billImportService";
import { createSlice } from "@reduxjs/toolkit";

const billImportSlice = createSlice({
  name: "billImport",
  initialState: [],
  reducers: {},
  extraReducers: (builder) => {
    builder.addCase(GET_ALL_BILL_IMPORT_BY_STORAGE_ID.fulfilled, (state, action) => {
      return action.payload.content;
    })
  }
})

export const { } = billImportSlice.actions;
export default billImportSlice.reducer;