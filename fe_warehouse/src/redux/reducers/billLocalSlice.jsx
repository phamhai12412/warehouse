import { createSlice } from "@reduxjs/toolkit";

const billLocalSlice = createSlice({
  name: "billLocal",
  initialState: {
    startStorageId: '',
    endStorageId: '',
    shipmentId: '',
    products: []
  },
  reducers: {
    setBillLocal: (state, action) => {
      return action.payload;
    },
    setStartStorageId: (state, action) => {
      return { ...state, startStorageId: action.payload };
    },
    removeStartStorageId: (state) => {
      return { ...state, startStorageId: '' };
    },
    setEndStorageId: (state, action) => {
      return { ...state, endStorageId: action.payload };
    },
    removeEndStorageId: (state) => {
      return { ...state, endStorageId: '' };
    },
    setShipmentId: (state, action) => {
      return { ...state, shipmentId: action.payload };
    },
    addProduct: (state, action) => {
      let check;
      for (let i = 0; i < state.products.length; i++) {
        if (state.products[i].id === action.payload.id) {
          check = state.products[i];
        }
      }
      if (check) {
        if (check.quantity < check.stock) {
          check.quantity += 1;
          check.total += check.price;
        }
      } else {
        state.products.push(action.payload)
      }
    },
    plusProduct: (state, action) => {
      for (let i = 0; i < state.products.length; i++) {
        if (state.products[i].id === action.payload) {
          if (state.products[i].quantity < state.products[i].stock) {
            state.products[i].quantity += 1;
            state.products[i].total += state.products[i].price
          }
        }
      }
    },
    changeQuantity: (state, action) => {
      for (let i = 0; i < state.products.length; i++) {
        if (state.products[i].id === action.payload.id) {
          if (action.payload.quantity > state.products[i].stock) {
            state.products[i].quantity = action.payload.quantity;              //state.products[i].stock;
            state.products[i].total = state.products[i].stock * state.products[i].price;
          } else if (action.payload.quantity <= 0) {
            state.products[i].quantity = 1;
            state.products[i].total = state.products[i].price;
          } else {
            state.products[i].quantity = action.payload.quantity;
            state.products[i].total = action.payload.quantity * state.products[i].price;
          }
        }
      }
    },
    minusProduct: (state, action) => {
      for (let i = 0; i < state.products.length; i++) {
        if (state.products[i].id === action.payload) {
          if (state.products[i].quantity === 1) {
            state.products = state.products.filter((item) => item.id !== action.payload);
          } else {
            state.products[i].quantity -= 1;
            state.products[i].total -= state.products[i].price
          }
        }
      }
    },
    removeProduct: (state, action) => {
      state.products = state.products.filter((item) => item.id !== action.payload)
      return state;
    },
    resetBillLocal: (state) => {
      return {
        startStorageId: '',
        endStorageId: '',
        shipmentId: '',
        products: []
      }
    }
  }
})

export const { setBillLocal, setStartStorageId, removeStartStorageId, setEndStorageId, removeEndStorageId, setShipmentId, addProduct, plusProduct, changeQuantity, minusProduct, removeProduct, resetBillLocal } = billLocalSlice.actions;
export default billLocalSlice.reducer;