import { ADD_SHIP, CHANGE_STATUS_SHIP, GET_ALL_SHIP, UPDATE_SHIP } from "../api/service/shipService";

import { createSlice } from "@reduxjs/toolkit";

const shipSlice = createSlice({
  name: 'ship',
  initialState: [],
  reducers: {
    changeStatusShip: (state, action) => {
      return state.map(item => {
        if (item.id === action.payload) {
          item = action.payload;
        }
      })
    }
  },
  extraReducers: (builder) => {
    builder
      .addCase(GET_ALL_SHIP.fulfilled, (state, action) => {
        return action.payload.content;
      })
      .addCase(ADD_SHIP.fulfilled, (state, action) => {
        return [...state, action.payload];
      })
      .addCase(UPDATE_SHIP.fulfilled, (state, action) => {
        return state.map(item => {
          if (item.id === action.payload.id) {
            item = action.payload;
          }
        })
      })
  }
})

export const { changeStatusShip } = shipSlice.actions;
export default shipSlice.reducer;