import { GET_ALL_ZONE } from "../api/service/zoneService";
import { createSlice } from "@reduxjs/toolkit";

const zoneSlice = createSlice({
  name: 'zone',
  initialState: [],
  reducers: {

  },
  extraReducers: (builder) => {
    builder.addCase(GET_ALL_ZONE.fulfilled, (state, action) => {
      return action.payload.content;
    })
  }
})

export const { } = zoneSlice.actions;
export default zoneSlice.reducer;