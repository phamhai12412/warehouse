import { Button, Pagination, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TextField } from '@mui/material'
import React, { useEffect, useState } from 'react'
import { TIME_OUT, debouncing } from '../../utils/Deboucing';
import { useNavigate, useParams } from 'react-router-dom';

import instance from '../../redux/api';

function DetailList() {

  const { id, storageId, storageName } = useParams();

  const navigate = useNavigate();

  const handleGoBack = () => {
    navigate(`/admin/reports/inventory/detail/${storageId}/${storageName}`);
  }

  const [search, setSearch] = useState('');
  const handleChangeSearch = (e) => setSearch(e.target.value);

  const [data, setData] = useState(null);
  const handleLoadDetail = () => {
    instance.get(`/api/v1/inventory/indAllByCompareInventory/${id}/?search=${search}`)
      .then(resp => setData(resp.data.content))
      .catch(err => console.log(err))
  }

  useEffect(() => {
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
                  <TableCell rowSpan={2} align="left">MÃ SẢN PHÂM</TableCell>
                  <TableCell rowSpan={2} align="center">GIÁ THÀNH</TableCell>
                  <TableCell colSpan={2} align='center'>SỐ LƯỢNG</TableCell>
                  <TableCell rowSpan={2} align='center'>LÝ DO</TableCell>
                </TableRow>
                <TableRow>
                  <TableCell align='center'>SỐ LƯỢNG HỆ THỐNG</TableCell>
                  <TableCell align='center'>SỐ LƯỢNG BÁO CÁO</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {data && data.map((item, index) => {
                  return (
                    <TableRow
                      key={item?.id}
                      sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                      className='hover:bg-slate-200 transition-all'
                    >
                      <TableCell align='center'>{index + 1}</TableCell>
                      <TableCell align='left'>{item?.product.productName} </TableCell>
                      <TableCell align='left'>{item?.product.code}</TableCell>
                      <TableCell align='center'>{item?.product.price.toLocaleString()}</TableCell>
                      <TableCell align='center'>{item?.quantityToday}</TableCell>
                      <TableCell align='center'>{item?.quantity}</TableCell>
                      <TableCell align='center'>{item?.reason}</TableCell>
                    </TableRow>
                  )
                })}
              </TableBody>
            </Table>
          </TableContainer>
        </div>
      </div>
    </div>
  )
}

export default DetailList