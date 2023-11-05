import { Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TextField } from '@mui/material';
import React, { useEffect, useState } from 'react'
import { TIME_OUT, debouncing } from '../../utils/Deboucing';
import { useDispatch, useSelector } from 'react-redux';

import { DATA_STORAGE } from '../../redux/selectors/selectors';
import { GET_ALL_STORAGE } from '../../redux/api/service/storageService';
import { Link } from 'react-router-dom';

function Product() {

  const storage = useSelector(DATA_STORAGE);

  const [value, setValue] = useState(storage[0]?.id);

  const [reload, setReload] = useState(false);
  const handleReload = () => setReload(!reload);

  const handleChange = (event, newValue) => {
    setValue(newValue);
  };

  const dispatch = useDispatch();

  const [search, setSearch] = useState('');
  const handleChangeSearch = (e) => setSearch(e.target.value);



  useEffect(() => {
    dispatch(GET_ALL_STORAGE(search));
  }, [reload, search])

  return (
    <div>
      <div className='flex justify-center text-3xl font-semibold uppercase'>quản lý sản phẩm trong từng kho</div>
      <div className='flex justify-between'>
      </div>
      <div className="content  w-full mt-5 ">
        <div className="header bg-white p-6 shadow-md">
          <TextField onChange={debouncing(handleChangeSearch, TIME_OUT)} id="outlined-basic" size='small' fullWidth label="Tìm kiếm kho" name='search' variant="outlined" />
        </div>
        <div className="table w-full mt-5">
          <TableContainer component={Paper} >
            <Table sx={{ width: '100%' }} aria-label="simple table">
              <TableHead>
                <TableRow>
                  <TableCell align="center">STT</TableCell>
                  <TableCell align="center">TÊN KHO</TableCell>
                  <TableCell align='center'>ĐỊA CHỈ</TableCell>
                  <TableCell align='center'>TYPE</TableCell>
                  <TableCell align='center'>KHU VỰC</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {storage && storage.map((e, index) => {
                  return (
                    <TableRow
                      key={e.id}
                      sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                    >
                      <TableCell align='center'>{index + 1}</TableCell>
                      <TableCell align='center'>
                        <Link to={`/admin/product/${e.id}/${e.storageName}`} className='text-blue-700 underline hover:text-red-500 transition-all'>{e.storageName}</Link>
                      </TableCell>
                      <TableCell align='center'>{e.address.toUpperCase()} </TableCell>
                      <TableCell align='center'>{e.typeStorage} </TableCell>
                      <TableCell align='center'>{e.zone.zoneName}</TableCell>
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

export default Product