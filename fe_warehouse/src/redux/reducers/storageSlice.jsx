import { GET_ALL_STORAGE } from "../api/service/storageService";
import { createSlice } from "@reduxjs/toolkit";

const storageSlice = createSlice({
  name: 'storage',
  initialState: [],
  reducers: {

  },
  extraReducers: (builder) => {
    builder.addCase(GET_ALL_STORAGE.fulfilled, (state, action) => {
      return action.payload.content;
    })
  }
})

export const { } = storageSlice.actions;
export default storageSlice.reducer;