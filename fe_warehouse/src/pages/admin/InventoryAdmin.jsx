import React, { useEffect } from 'react'
import { TIME_OUT, debouncing } from '../../utils/Deboucing';
import { Table, TableBody, TableCell, TableHead, TableRow, TextField } from '@mui/material'
import { useDispatch, useSelector } from 'react-redux'

import { DATA_STORAGE } from '../../redux/selectors/selectors';
import { GET_ALL_STORAGE } from '../../redux/api/service/storageService';
import { Link } from 'react-router-dom';
import { useState } from 'react';

function InventoryAdmin() {

  const storage = useSelector(DATA_STORAGE);
  const dispatch = useDispatch();

  const [search, setSearch] = useState('');
  const handleChangeSearch = (e) => setSearch(e.target.value);

  useEffect(() => {
    dispatch(GET_ALL_STORAGE(search));
  }, [search])

  return (
    <div>
      <div className='flex justify-center text-3xl font-semibold uppercase py-5'>quản lý tồn kho trong từng kho</div>
      <div className='flex flex-col gap-5 '>
        <div className="header bg-white p-6 shadow-md">
          <TextField onChange={debouncing(handleChangeSearch, TIME_OUT)} id="outlined-basic" size='small' fullWidth label="Tìm kiếm theo tên kho" name='search' variant="outlined" />
        </div>
      </div>
      <div className="content w-full mt-5">
        <Table className='bg-white' sx={{ width: '100%' }} aria-label="simple table">
          <TableHead>
            <TableRow>
              <TableCell align="center">STT</TableCell>
              <TableCell align="center">TÊN KHO</TableCell>
              <TableCell align='center'>ĐỊA CHỈ</TableCell>
              <TableCell align='center'>SỐ DIỆN THOẠI</TableCell>
              <TableCell align='center'>TYPE</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {storage && storage.map((item, index) => {
              return (
                <TableRow
                  key={item?.id}
                  sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                >
                  <TableCell align='center'>{index + 1}</TableCell>
                  <TableCell align='center'><Link to={`/admin/reports/inventory/detail/${item.id}/${item.storageName}`} className='underline text-blue-600'>{item?.storageName}</Link> </TableCell>
                  <TableCell align='center'>{item?.address.toUpperCase()}</TableCell>
                  <TableCell align='center'>{item?.users.phone}</TableCell>
                  <TableCell align='center'>{item?.typeStorage}</TableCell>
                </TableRow>
              )
            })}
          </TableBody>
        </Table>
      </div>
    </div>
  )
}

export default InventoryAdmin