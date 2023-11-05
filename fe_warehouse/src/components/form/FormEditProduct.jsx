import { Button, FormControl, InputLabel, MenuItem, Select, TextField } from '@mui/material';
import React, { useEffect, useState } from 'react'
import { getDownloadURL, ref, uploadBytesResumable } from "firebase/storage";
import { useDispatch, useSelector } from 'react-redux';
import { validateCreatedAndDueDate, validateNumber } from '../../utils/ValidationForm';

import AddPhotoAlternateIcon from '@mui/icons-material/AddPhotoAlternate';
import Box from '@mui/material/Box';
import { Cookies } from 'react-cookie';
import { DATA_CATEGORY } from '../../redux/selectors/selectors';
import { GET_ALL_CATEGORY } from '../../redux/api/service/categoryService';
import Modal from '@mui/material/Modal';
import { UPDATE_PRODUCT } from '../../redux/api/service/productService';
import { firebaseStorage } from '../../firebase/firebase'
import { formatDate } from '../../utils/FormatDate';
import { formatText } from '../../utils/FormatText';
import { validateExistsNameProductUpdate } from '../../utils/ValidateProduct';

function FormEditProduct({ openEdit, handleCloseEdit, handleReload, edit, storageId, products }) {

  const dispatch = useDispatch();
  const categories = useSelector(DATA_CATEGORY);

  const [category, setCategory] = React.useState('');
  const handleChangeCategory = (event) => {
    setCategory(event.target.value);
  };

  const handleCloseForm = () => {
    handleCloseEdit();
  }

  const [errorDate, setErrorDate] = useState('');
  const [errorPrice, setErrorPrice] = useState('');
  const [errorName, setErrorName] = useState('');
  const handleAddProduct = (e) => {
    e.preventDefault();
    if (validateNumber(e.target.price.value)) {
      setErrorPrice("Vui Lòng nhập lơn hơn 0");
      return;
    }

    if (validateExistsNameProductUpdate(products, e.target.productName.value, edit.productName, category, edit.createdDate, edit.dueDate)) {
      setErrorName("Đã có sản phẩm này trong kho");
      return;
    }

    if (validateCreatedAndDueDate(formatDate(e.target.created.value), formatDate(e.target.dueDate.value))) {
      setErrorDate("Lựa chọn ngày không đúng");
      return;
    }

    const result = {
      productName: formatText(e.target.productName.value),
      price: e.target.price.value,
      created: formatDate(e.target.created.value),
      dueDate: formatDate(e.target.dueDate.value),
      categoryId: category,
      storageId: storageId
    }
    console.log(result);
    dispatch(UPDATE_PRODUCT({ data: result, id: e.target.id.value }));
    setTimeout(() => {
      handleReload();
    }, 100)
    setCategory("");
    handleCloseEdit();
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
    setErrorDate('')
    setErrorPrice('');
    setErrorName('');
    dispatch(GET_ALL_CATEGORY(""));
  }, [openEdit])

  return (
    <Modal
      open={openEdit}
      onClose={handleCloseForm}
      aria-labelledby="modal-modal-title"
      aria-describedby="modal-modal-description"
    >
      <Box sx={style}>
        {errorName ? <p className='font-semibold text-lg text-red-600 text-center uppercase'>{errorName}</p> : ''}
        <form action="" onSubmit={handleAddProduct} encType="multipart/form-data">
          <TextField id="filled-basic" label="ID" name='id' defaultValue={edit.id} InputProps={{ readOnly: true }} variant="filled" sx={{ margin: '5px 0', display: 'none' }} fullWidth required />
          <TextField id="filled-basic" label="Mã sản phẩm" name='code' defaultValue={edit.code} InputProps={{ readOnly: true }} variant="filled" sx={{ margin: '5px 0' }} fullWidth required />
          {new Cookies().get('role') === 'ROLE_ADMIN' || edit.statusName === 'PENDING' ?
            <TextField id="filled-basic" label="Tên sản phẩm" name='productName' defaultValue={edit.productName} variant="filled" sx={{ margin: '5px 0' }} fullWidth required /> :
            <TextField id="filled-basic" label="Tên sản phẩm" InputProps={{ readOnly: true }} name='productName' defaultValue={edit.productName} variant="filled" sx={{ margin: '5px 0' }} fullWidth required />
          }
          <div style={{ display: 'flex', width: '100%', gap: '10px' }}>
            <TextField error={errorDate} label={errorDate ? errorDate : "Ngày Sản Xuất"} color="secondary" InputProps={{ readOnly: edit.statusName !== 'PENDING' }} defaultValue={edit.createdDate} focused name="created" type="date" sx={{ flex: 1, margin: "10px 0" }} required />
            <TextField error={errorDate} label={errorDate ? errorDate : "Ngày Hết Hạn"} color="secondary" InputProps={{ readOnly: edit.statusName !== 'PENDING' }} defaultValue={edit.dueDate} focused name="dueDate" type="date" sx={{ flex: 1, margin: "10px 0" }} required />
          </div>
          <TextField error={errorPrice} id="filled-basic" label={errorPrice ? errorPrice : "Giá tiền"} type="number" defaultValue={edit.price} name='price' variant="filled" sx={{ margin: '5px 0' }} fullWidth required />
          <br />
          <div className='flex gap-2 justify-center my-2'>
            <FormControl sx={{ width: '100%', margin: '5px 0' }}>
              <InputLabel id="demo-simple-select-label">Thể loại</InputLabel>
              <Select inputProps={{ readOnly: new Cookies().get('role') !== 'ROLE_ADMIN' && edit.statusName !== 'PENDING' }} labelId="demo-simple-select-label" id="demo-simple-select" multiline defaultValue={edit.category.id} label="Category" onChange={handleChangeCategory} required>
                {categories && categories.map(item => (item.status ? <MenuItem key={item.id} value={item.id}>{item.categoryName}</MenuItem> : ''))}
              </Select>
            </FormControl>
          </div>
          <Button type='submit' sx={{ mt: 2, width: "100%" }} variant="contained"> cập nhật </Button>
        </form>
      </Box >
    </Modal >
  )
}

export default FormEditProduct