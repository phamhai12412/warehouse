import { ACCEPT_PRODUCT, DELETE_PRODUCT } from '../../redux/api/service/productService';
import { Button, Table, TableBody, TableCell, TableHead, TableRow, Tooltip } from '@mui/material'
import React, { useEffect, useState } from 'react'

import CheckIcon from '@mui/icons-material/Check';
import DeleteIcon from '@mui/icons-material/Delete';
import instance from '../../redux/api'
import { useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';

function ProductPendingAdmin({ reload, handleReload }) {

  const dispatch = useDispatch();
  const navigate = useNavigate();


  const handleAccept = (item) => {
    dispatch(ACCEPT_PRODUCT(item.id));
    setTimeout(() => {
      handleReload();
    }, 100)
    navigate(`/admin/product/${item.storage.id}/${item.storage.storageName}`)
  }

  const handleReject = (id) => {
    dispatch(DELETE_PRODUCT(id));
    setTimeout(() => {
      handleReload();
    }, 100)

  }

  const [data, setData] = useState(null);
  const handleLoadProduct = () => {
    instance.get('/api/v1/products')
      .then((resp) => setData(resp.data.content))
      .catch((err) => console.log(err))
  }

  useEffect(() => {
    handleLoadProduct();
  }, [reload])

  return (
    <div className='bg-white'>
      {console.log(data)}
      <div className='w-full px-5 py-3'>
        <h1 className='uppercase font-semibold text-lg'>Sản Phẩm Chờ Duyệt</h1>
      </div>
      <Table sx={{ width: '100%' }} aria-label="simple table">
        <TableHead>
          <TableRow className='bg-slate-300'>
            <TableCell align="center">STT</TableCell>
            <TableCell align='center'>TÊN</TableCell>
            <TableCell align='center'>NGÀY SẢN XUẤT</TableCell>
            <TableCell align='center'>NGÀY HẾT HẠN</TableCell>
            <TableCell align="center">GIÁ THÀNH</TableCell>
            <TableCell align='center'>SỐ LƯỢNG</TableCell>
            <TableCell align='center'>THỂ LOẠI</TableCell>
            <TableCell align='center'>KHO</TableCell>
            <TableCell align='center'>HÀNH ĐỘNG</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {data && data.map((e, index) => {
            if (e?.statusName === "PENDING") {
              return (
                <TableRow
                  key={e?.id}
                  sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                  className='hover:bg-slate-100 transition-all'
                >
                  <TableCell onClick={() => navigate(`/admin/product/${e?.storage.id}/${e?.storage.storageName}`)} align='center'>{index + 1}</TableCell>
                  <TableCell onClick={() => navigate(`/admin/product/${e?.storage.id}/${e?.storage.storageName}`)} align='center'>{e?.productName} </TableCell>
                  <TableCell onClick={() => navigate(`/admin/product/${e?.storage.id}/${e?.storage.storageName}`)} align='center'>{e?.createdDate} </TableCell>
                  <TableCell onClick={() => navigate(`/admin/product/${e?.storage.id}/${e?.storage.storageName}`)} align='center'>{e?.dueDate}</TableCell>
                  <TableCell onClick={() => navigate(`/admin/product/${e?.storage.id}/${e?.storage.storageName}`)} align='center'>{e?.price.toLocaleString()}</TableCell>
                  <TableCell onClick={() => navigate(`/admin/product/${e?.storage.id}/${e?.storage.storageName}`)} align='center'>{e?.quantity}</TableCell>
                  <TableCell onClick={() => navigate(`/admin/product/${e?.storage.id}/${e?.storage.storageName}`)} align='center'>{e?.category.categoryName}</TableCell>
                  <TableCell align='center'>{e?.storage.address}</TableCell>
                  <TableCell align='center'>
                    <div className='flex gap-2 justify-center'>
                      <Button variant="contained" onClick={() => handleAccept(e)} color='warning'><Tooltip title="Chấp Thuận"><CheckIcon /></Tooltip></Button>
                      <Button variant="contained" onClick={() => handleReject(e.id)} color='error'><Tooltip title='Từ Chối'><DeleteIcon /></Tooltip></Button>
                    </div>
                  </TableCell>
                </TableRow>
              )
            }
          })}
        </TableBody>
      </Table>
    </div>
  )
}

export default ProductPendingAdmin