import { Button, Pagination, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TextField } from '@mui/material'
import React, { useEffect, useState } from 'react'
import { TIME_OUT, debouncing } from '../../utils/Deboucing';
import { useNavigate, useParams } from 'react-router-dom';
import { validateBlank, validateNumber } from '../../utils/ValidationForm';

import CloseIcon from '@mui/icons-material/Close';
import { Cookies } from 'react-cookie';
import instance from '../../redux/api';
import { validateTime } from '../../utils/ValidateTime';

function DetailListMarket() {

  const { id, created } = useParams();

  const navigate = useNavigate();

  const [data, setData] = useState(null);

  const handleGoBack = () => {
    navigate(`/market/reports/inventory`);
  }

  const [quantity, setQuantity] = useState(0);
  const handleChangeQuantity = (e) => setQuantity(e.target.value);
  const [reason, setReason] = useState('');
  const handleChangeReason = (e) => setReason(e.target.value);

  const handleEdit = (item) => {
    let newArr = [...data];
    for (let i = 0; i < newArr.length; i++) {
      if (newArr[i].id === item.id) {
        setQuantity(newArr[i].quantity);
        setReason(newArr[i].reason);
        newArr[i].isEdit = true;
      } else {
        newArr[i].isEdit = false;
      }
    }
    setData(newArr);
    setErrorQuantity('');
  }

  const handleCloseEdit = () => {
    let newArr = [...data];
    for (let i = 0; i < newArr.length; i++) {
      if (newArr[i].isEdit) {
        newArr[i].isEdit = false;
      }
    }
    setData(newArr);
    setErrorQuantity('');
  }

  const [errorQuantity, setErrorQuantity] = useState('');
  const [errorReason, setErrorReason] = useState('');
  const handleUpdate = async (item) => {

    if (validateNumber(quantity)) {
      setErrorQuantity("Số lượng không được bé hơn 0");
      return;
    }
    if (quantity > item.product.quantity) {
      setErrorQuantity("Số lượng báo cáo sai");
      return;
    }
    if (validateBlank(reason)) {
      setErrorReason("Lý do không được bỏ trống");
      return;
    }

    const result = {
      productId: item.product.id,
      quantity: quantity,
      reason: reason
    }
    console.log("edit ", item)
    await instance.put(`/api/v1/inventory/inventoryDetail/${item.id}`, result, {
      headers: {
        Authorization: `Bearer ${new Cookies().get('token')}`
      }
    }).then((resp) => {
      let newArr = [...data];
      for (let i = 0; i < newArr.length; i++) {
        if (newArr[i].id === resp.data.id) {
          newArr[i] = { ...resp.data, isEdit: false };
        }
      }
    }).catch((err) => console.log(err));
    handleLoadDetail();
  }

  const [search, setSearch] = useState('');
  const handleChangeSearch = (e) => setSearch(e.target.value);

  const handleLoadDetail = () => {
    instance.get(`/api/v1/inventory/indAllByCompareInventory/${id}/?search=${search}`)
      .then(resp => setData(resp.data.content.map(item => ({ ...item, isEdit: false }))))
      .catch(err => console.log(err))
  }



  useEffect(() => {
    setErrorQuantity('');
    handleLoadDetail();
  }, [search])

  return (
    <div className='mx-20'>
      <div className="action py-2">
        <Button variant="contained" onClick={handleGoBack} >Quay Lại</Button>
      </div>
      <div className='bg-white'>
        <div className="header p-3 text-center">
          <h1 className='text-center text-4xl mb-5'>BÁO CÁO TỒN KHO</h1>
        </div>
        <div className="search px-3">
          <TextField id="outlined-basic" onChange={debouncing(handleChangeSearch, TIME_OUT)} label="Tìm kiếm theo tên sản phẩm" variant="outlined" fullWidth size='small' />
        </div>
        <div className="content w-full table p-3">
          <TableContainer component={Paper} >
            <Table sx={{ width: '100%' }} aria-label="simple table">
              <TableHead className='bg-slate-200'>
                <TableRow>
                  <TableCell rowSpan={2} align="center">STT</TableCell>
                  <TableCell rowSpan={2} align="left">TÊN</TableCell>
                  <TableCell rowSpan={2} align="left">MÃ SẢN PHẨM</TableCell>
                  <TableCell rowSpan={2} align="center">GIÁ THÀNH</TableCell>
                  <TableCell colSpan={2} align='center'>SỐ LƯỢNG</TableCell>
                  <TableCell rowSpan={2} align='center'>LÝ DO</TableCell>
                  {validateTime(created) ? <TableCell rowSpan={2} align='center'>HÀNH ĐỘNG</TableCell> : ''}
                </TableRow>
                <TableRow>
                  <TableCell align='center'>SỐ LƯỢNG HỆ THỐNG</TableCell>
                  <TableCell align='center'>SỐ LƯỢNG BÁO CÁO</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {data && data.map((item, index) => {
                  if (validateTime(created)) {
                    if (item.isEdit) {
                      return (
                        <TableRow
                          key={item?.id}
                          sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                          className='hover:bg-slate-100 transition-all'
                        >
                          <TableCell align='center'><CloseIcon onClick={handleCloseEdit} /></TableCell>
                          <TableCell align='left'>{item?.product.productName} </TableCell>
                          <TableCell align='left'>{item?.product.code} </TableCell>
                          <TableCell align='center'>{item?.product.price.toLocaleString()}</TableCell>
                          <TableCell align='center'>{item?.product.quantity}</TableCell>
                          <TableCell align='center'>
                            <TextField
                              error={errorQuantity}
                              id="outlined-basic" type='number'
                              defaultValue={quantity} onChange={handleChangeQuantity}
                              label={errorQuantity ? errorQuantity : "Số lượng báo cáo"}
                              variant="outlined" size='small'
                            />
                          </TableCell>
                          <TableCell align='center'>
                            <TextField id="outlined-basic"
                              error={errorReason}
                              defaultValue={reason} onChange={handleChangeReason}
                              label={errorReason ? errorReason : "Lý do"}
                              variant="outlined" size='small' fullWidth
                            />
                          </TableCell>
                          <TableCell align='center'><Button variant="contained" onClick={() => handleUpdate(item)} >Cập Nhật</Button></TableCell>
                        </TableRow>
                      )
                    } else {
                      return (
                        <TableRow
                          key={item?.id}
                          sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                          className='hover:bg-slate-100 transition-all'
                        >
                          <TableCell align='center'>{index + 1}</TableCell>
                          <TableCell align='left'>{item?.product.productName} </TableCell>
                          <TableCell align='left'>{item?.product.code} </TableCell>
                          <TableCell align='center'>{item?.product.price.toLocaleString()}</TableCell>
                          <TableCell align='center'>{item?.product.quantity}</TableCell>
                          <TableCell align='center'>{item?.quantity}</TableCell>
                          <TableCell align='center'>{item?.reason}</TableCell>
                          <TableCell align='center'><Button variant="contained" onClick={() => handleEdit(item)} >Sửa</Button></TableCell>
                        </TableRow>
                      )
                    }
                  } else {
                    return (
                      <TableRow
                        key={item?.id}
                        sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                        className='hover:bg-slate-100 transition-all'
                      >
                        <TableCell align='center'>{index + 1}</TableCell>
                        <TableCell align='left'>{item?.product.productName} </TableCell>
                        <TableCell align='left'>{item?.product.code} </TableCell>
                        <TableCell align='center'>{item?.product.price.toLocaleString()}</TableCell>
                        <TableCell align='center'>{item?.quantityToday}</TableCell>
                        <TableCell align='center'>{item?.quantity}</TableCell>
                        <TableCell align='center'>{item?.reason}</TableCell>
                      </TableRow>
                    )
                  }
                })}
              </TableBody>
            </Table>
          </TableContainer>
        </div>
      </div>
    </div>
  )
}

export default DetailListMarket