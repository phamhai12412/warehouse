import { Button, FormControl, InputLabel, MenuItem, Select as SelectMui, TextField } from '@mui/material';
import React, { useEffect, useState } from 'react'
import { getDownloadURL, ref, uploadBytesResumable } from "firebase/storage";
import { useDispatch, useSelector } from 'react-redux';
import { validateCreatedAndDueDate, validateNumber } from '../../utils/ValidationForm';

import { ADD_PRODUCT } from '../../redux/api/service/productService';
import AddPhotoAlternateIcon from '@mui/icons-material/AddPhotoAlternate';
import Box from '@mui/material/Box';
import { DATA_CATEGORY } from '../../redux/selectors/selectors';
import { GET_ALL_CATEGORY } from '../../redux/api/service/categoryService';
import Modal from '@mui/material/Modal';
import { firebaseStorage } from '../../firebase/firebase'
import { formatDate } from '../../utils/FormatDate';
import { formatText } from '../../utils/FormatText';

function FormProduct({ open, handleClose, handleReload, storageId, setLoading }) {

  const dispatch = useDispatch();
  const categories = useSelector(DATA_CATEGORY);

  const [category, setCategory] = React.useState('');
  const handleChangeCategory = (event) => {
    setCategory(event.target.value);
  };

  const handleCloseForm = () => {
    handleClose();
  }

  const [errorDate, setErrorDate] = useState('');
  const [errorQuantity, setErrorQuantity] = useState('');
  const [errorPrice, setErrorPrice] = useState('');
  const [errorWeight, setErrorWeight] = useState('');
  const handleAddProduct = (e) => {
    e.preventDefault();
    if (validateCreatedAndDueDate(formatDate(e.target.created.value), formatDate(e.target.dueDate.value))) {
      setErrorDate("Lựa chọn ngày không đúng");
      return;
    }
    if (validateNumber(e.target.quantity.value)) {
      setErrorQuantity("Vui Lòng nhập lơn hơn 0");
      return;
    }
    if (validateNumber(e.target.price.value)) {
      setErrorPrice("Vui Lòng nhập lơn hơn 0");
      return;
    }
    if (validateNumber(e.target.weight.value)) {
      setErrorWeight("Vui Lòng nhập lơn hơn 0");
      return;
    }


    const result = {
      productName: formatText(e.target.productName.value),
      created: formatDate(e.target.created.value),
      dueDate: formatDate(e.target.dueDate.value),
      quantity: e.target.quantity.value,
      price: e.target.price.value,
      weight: e.target.weight.value,
      categoryId: category,
      storageId: storageId
    }
    // console.log(result)
    dispatch(ADD_PRODUCT(result));
    setCategory("");
    setTimeout(() => {
      handleReload();
    }, 200)
    handleClose();
    if (setLoading) {
      setLoading();
    }
  }

  const style = {
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: 500,
    bgcolor: 'background.paper',
    border: '2px solid #000',
    boxShadow: 24,
    p: 4,
  };

  useEffect(() => {
    setErrorDate('');
    setErrorQuantity('');
    setErrorPrice('');
    setErrorWeight('');
    dispatch(GET_ALL_CATEGORY(""));
  }, [open])

  return (
    <Modal
      open={open}
      onClose={handleCloseForm}
      aria-labelledby="modal-modal-title"
      aria-describedby="modal-modal-description"
    >
      <Box sx={style}>
        <form action="" onSubmit={handleAddProduct} encType="multipart/form-data">
          <TextField id="filled-basic" label="Tên sản phẩm" name='productName' variant="filled" sx={{ margin: '5px 0' }} fullWidth required />
          <div style={{ display: 'flex', width: '100%', gap: '10px' }}>
            <TextField error={errorDate} label={errorDate ? errorDate : "Ngày Sản Xuất"} color="secondary" focused name="created" type="date" sx={{ flex: 1, margin: "10px 0" }} required />
            <TextField error={errorDate} label={errorDate ? errorDate : "Ngày Hết Hạn"} color="secondary" focused name="dueDate" type="date" sx={{ flex: 1, margin: "10px 0" }} required />
          </div>
          <TextField error={errorQuantity} id="filled-basic" label={errorQuantity ? errorQuantity : "Số lượng"} type="number" name='quantity' variant="filled" sx={{ margin: '5px 0' }} fullWidth required />
          <br />
          <TextField error={errorPrice} id="filled-basic" label={errorPrice ? errorPrice : "Giá Thành"} type="number" name='price' variant="filled" sx={{ margin: '5px 0' }} fullWidth required />
          <br />
          <TextField error={errorWeight} id="filled-basic" label={errorWeight ? errorWeight : "Cân nặng"} type="number" name='weight' variant="filled" sx={{ margin: '5px 0' }} fullWidth required />
          <br />
          <FormControl sx={{ width: '100%', margin: '5px 0' }}>
            <InputLabel id="demo-simple-select-label">Thể loại</InputLabel>
            <SelectMui labelId="demo-simple-select-label" id="demo-simple-select" multiline value={category} label="Category" onChange={handleChangeCategory} required>
              {categories && categories.map(item => (item.status ? <MenuItem key={item.id} value={item.id}>{item.categoryName}</MenuItem> : ''))}
            </SelectMui>
          </FormControl>
          <Button type='submit' sx={{ mt: 2, width: "100%" }} variant="contained"> THÊM </Button>
        </form>
      </Box >
    </Modal >
  )
}

export default FormProduct