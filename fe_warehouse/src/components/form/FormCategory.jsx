import { Button, TableCell, TableRow, TextField } from '@mui/material'

import CloseIcon from '@mui/icons-material/Close';
import React from 'react'

function FormCategory({ result, handleCloseForm, handleChangeForm, handleAddCategory, errorCategoryName }) {

  const styled1 = { padding: '4px 10px', borderRadius: '5px', border: '2px solid #000', outline: 'none', width: '50%', textAlign: 'center' };


  return (
    <TableRow>
      <TableCell align='center'><CloseIcon onClick={handleCloseForm} /></TableCell>
      <TableCell align='center'>
        <TextField
          error={errorCategoryName}
          id="outlined-basic"
          autoFocus fullWidth
          size='small'
          label={errorCategoryName ? errorCategoryName : "Tên thể loại"}
          defaultValue={result.categoryName}
          onChange={handleChangeForm}
          name="categoryName"
          variant="outlined"
          required
        />
      </TableCell>
      <TableCell align='center'><i className="fa-solid fa-lock-open"></i></TableCell>
      <TableCell align='center'><Button variant="contained" onClick={handleAddCategory}>thêm</Button></TableCell>
    </TableRow>
  )
}

export default FormCategory